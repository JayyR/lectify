package org.lectify.query.handler.book;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.lectify.common.query.book.FindBook;
import org.lectify.common.query.book.FindByAuthor;
import org.lectify.common.query.book.FindByIsbn;
import org.lectify.common.query.book.FindByTitle;
import org.lectify.common.value.book.BookItem;
import org.lectify.query.model.entity.IndustryIdentifier;
import org.lectify.query.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class BookQueryHandler {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookQueryHandler(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @QueryHandler
    public BookItem handle(FindByIsbn findByIsbn){
        log.info("Handling::{}",findByIsbn);
        return bookRepository.findByIndustryIdentifiersContaining(new IndustryIdentifier(findByIsbn.getType(),
                findByIsbn.getIsbn()))
                .map(book -> modelMapper.map(book,BookItem.class))
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage());
                    return Mono.empty();
                })
                .log()
                .block();
    }

    @QueryHandler
    public List<BookItem> handle(FindByTitle findByTitle){
        log.info("Handling::{}",findByTitle);
        return handle(new FindBook(findByTitle.getTitle()));
    }

    @QueryHandler
    public List<BookItem> handle(FindByAuthor findByAuthor){
        log.info("Handling::{}",findByAuthor);
        return handle(new FindBook(findByAuthor.getAuthor()));
    }

    @QueryHandler
    public List<BookItem> handle(FindBook findBook){
        log.info("Handling::{}",findBook);
        TextCriteria titleCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(new Term(findBook.getSearchTerm(), Term.Type.PHRASE).getFormatted());
        return bookRepository.findAllByOrderByScoreDesc(titleCriteria)
                .map(book -> modelMapper.map(book,BookItem.class))
                .log()
                .collectList().block();
    }
}
