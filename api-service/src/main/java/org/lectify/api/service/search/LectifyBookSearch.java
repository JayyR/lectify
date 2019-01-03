package org.lectify.api.service.search;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.lectify.common.query.book.FindByIsbn;
import org.lectify.common.query.book.FindByTitle;
import org.lectify.common.value.book.BookItem;
import org.lectify.common.query.book.FindBook;
import org.lectify.common.query.book.FindByAuthor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class LectifyBookSearch implements BookSearch{

    private final QueryGateway queryGateway;
    private final ModelMapper modelMapper;

    @Autowired
    public LectifyBookSearch(QueryGateway queryGateway, ModelMapper modelMapper) {
        this.queryGateway = queryGateway;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<BookItem> isbnSearch(String isbn) {
        log.info("isbnSearch");
        String isbnType = "";
        if(isbn.length()==10){
            isbnType = "ISBN_10";
        }else{
            isbnType = "ISBN_13";
        }
        FindByIsbn findByIsbn = new FindByIsbn(isbnType,isbn);
        return Mono.fromFuture(queryGateway.query(findByIsbn,ResponseTypes.instanceOf(BookItem.class)))
                .onErrorResume(throwable -> Mono.never());

    }

    @Override
    public Flux<BookItem> titleSearch(String title) {
        FindByTitle findByTitle = new FindByTitle(title);
        return Flux.fromIterable(
                queryGateway.query(findByTitle,ResponseTypes.multipleInstancesOf(BookItem.class))
                        .join()
        ).log();
    }

    @Override
    public Flux<BookItem> authorSearch(String author) {
        FindByAuthor findByAuthor = new FindByAuthor(author);
        return Flux.fromIterable(
                queryGateway.query(findByAuthor,ResponseTypes.multipleInstancesOf(BookItem.class))
                        .join()
        ).log();
    }

    @Override
    public Flux<BookItem> search(String searchString) {
        FindBook findBook = new FindBook(searchString);
        return Flux.fromIterable(
                queryGateway.query(findBook,ResponseTypes.multipleInstancesOf(BookItem.class))
                        .join()
        ).log();
    }
}
