package org.lectify.query.repository;

import org.lectify.query.model.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<User,UUID> {

    Mono<User> findByEmailId(String emailId);
}
