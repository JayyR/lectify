package org.lectify.api.service.search;

import lombok.extern.slf4j.Slf4j;
import org.lectify.common.value.book.BookItem;
import org.lectify.api.service.search.model.enums.SearchBy;
import org.lectify.api.service.search.model.enums.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.EnumMap;
import java.util.Map;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {


    private final Map<SearchType, BookSearch> searchEngineMap;

    @Autowired
    public SearchServiceImpl(GoogleBookSearch googleBookSearch, LectifyBookSearch lectifyBookSearch) {
        searchEngineMap = new EnumMap<>(SearchType.class);
        searchEngineMap.put(SearchType.EXTERNAL,googleBookSearch);
        searchEngineMap.put(SearchType.INTERNAL,lectifyBookSearch);

    }

    @Override
    public Flux<BookItem> bookSearch(SearchType searchType, SearchBy searchBy, String parameter) {

        BookSearch bookSearch = searchEngineMap.get(searchType);

        searchBy = searchBy == null ? SearchBy.DEFAULT : searchBy;
        switch (searchBy) {
            case ISBN:
                return bookSearch.isbnSearch(parameter).flux();
            case TITLE:
               return bookSearch.titleSearch(parameter);
            case AUTHOR:
                return bookSearch.authorSearch(parameter);
            default:
                return bookSearch.search(parameter);
        }

    }




}
