package org.lectify.api.service.user;


import org.lectify.common.value.book.LectifyUser;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {

    default Mono<LectifyUser> getLectifyUser(UUID uuid){
        throw new UnsupportedOperationException();
    };

    default Mono<LectifyUser> getLectifyUser(String emailId){
        throw new UnsupportedOperationException();
    }

    default Mono<LectifyUser> createLectifyUser(String emailId){
        throw new UnsupportedOperationException();
    }
}
