package org.lectify.query.repository;

import org.lectify.query.model.entity.Book;
import org.lectify.query.model.entity.IndustryIdentifier;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BookRepository extends ReactiveMongoRepository<Book,UUID>{

    Mono<Book> findByIndustryIdentifiersContaining(IndustryIdentifier industryIdentifier);

    Flux<Book> findAllByOrderByScoreDesc(TextCriteria textCriteria);
}
