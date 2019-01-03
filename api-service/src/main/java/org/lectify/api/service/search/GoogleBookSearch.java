package org.lectify.api.service.search;

import lombok.extern.slf4j.Slf4j;
import org.lectify.common.value.book.BookItem;
import org.lectify.api.service.search.model.GoogleSearchResult;
import org.lectify.api.service.search.model.GoogleBookItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class GoogleBookSearch implements BookSearch {

    private static final String GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String ISBN_SEARCH_FORMAT = "isbn:%s";
    private static final String IN_TITLE_SEARCH_FORMAT = "intitle:%s";
    private static final String IN_AUTHOR_SEARCH_FORMAT = "inauthor:%s";
    private static final String SUBJECT_SEARCH_FORMAT = "subject:%s";
    private static final int MAX_SEARCH_RESULTS = 10;
    private static final String GZIP_ENCODING = "gzip";
    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final MultiValueMap<String, String> queryParams;

    @Autowired
    public GoogleBookSearch(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        webClient = WebClient.builder()
                .baseUrl(GOOGLE_BOOKS_BASE_URL)
                .build();

        queryParams = new LinkedMultiValueMap<String, String>() {
            {
                set("printType", "books");
                set("maxResults", Integer.toString(MAX_SEARCH_RESULTS));
            }
        };
    }

    @Override
    public Mono<BookItem> isbnSearch(String isbn) {
        log.info("isbnSearch");
        queryParams.set("q", String.format(ISBN_SEARCH_FORMAT, isbn));
        return doSearch(queryParams)
                .flatMapIterable(googleSearchResult -> googleSearchResult.getItems())
                .filter(this.matchIndustryIdentifiers())
                .next()
                .map(googleBookItem -> modelMapper.map(googleBookItem.getVolumeInfo(), BookItem.class))
                .onErrorResume(throwable -> Mono.empty());
    }

    @Override
    public Flux<BookItem> titleSearch(String title) {
        queryParams.set("q", String.format(IN_TITLE_SEARCH_FORMAT, title));

        return doRecursiveSearch(queryParams);
    }

    @Override
    public Flux<BookItem> authorSearch(String author) {
        queryParams.set("q", String.format(IN_AUTHOR_SEARCH_FORMAT, author));

        return doRecursiveSearch(queryParams);
    }


    @Override
    public Flux<BookItem> search(String searchString) {
        queryParams.set("q", searchString);

        return doRecursiveSearch(queryParams);
    }

    private Flux<BookItem> doRecursiveSearch(MultiValueMap<String, String> queryParams) {
        queryParams.set("startIndex", Integer.toString(0));
        return doSearch(queryParams)
                .expand(googleSearchResult -> {
                    int totalItems = (googleSearchResult.getTotalItems() > MAX_SEARCH_RESULTS) ?
                             MAX_SEARCH_RESULTS : googleSearchResult.getTotalItems();
                    int resultSize = googleSearchResult.getItems().size();
                    int nextFrom = Integer.parseInt(queryParams.getFirst("startIndex")) + resultSize;
                    if (nextFrom >= totalItems) {
                        return Mono.empty();
                    }
                    queryParams.set("startIndex", Integer.toString(nextFrom));
                    return doSearch(queryParams);

                })
                .flatMapIterable(googleSearchResult -> googleSearchResult.getItems())
                .filter(this.matchIndustryIdentifiers())
                .map(googleBookItem -> modelMapper.map(googleBookItem.getVolumeInfo(), BookItem.class)).log();
    }

    private Mono<GoogleSearchResult> doSearch(MultiValueMap<String, String> queryParams) {
        log.info("Query Params :{}", queryParams);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParams(queryParams).build())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_ENCODING, GZIP_ENCODING)
                .retrieve()
                .bodyToMono(GoogleSearchResult.class)
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage());
                    return Mono.empty();
                }).log();
    }

    private Predicate<GoogleBookItem> matchIndustryIdentifiers() {
        List<String> validIsbnTypes = Arrays.asList("ISBN_10", "ISBN_13");
        return googleBookItem -> googleBookItem.getVolumeInfo().getIndustryIdentifiers()
                .stream()
                .anyMatch(industryIdentifiers -> {
                    String type = industryIdentifiers.getType();
                    return validIsbnTypes.stream().anyMatch(type::equals);
                });
    }
}
