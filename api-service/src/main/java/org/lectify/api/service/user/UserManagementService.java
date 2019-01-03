package org.lectify.api.service.user;


import org.lectify.api.service.user.model.UserRegistrationRequest;
import org.lectify.api.service.user.model.UserRegistrationResponse;
import reactor.core.publisher.Mono;

public interface UserManagementService {

    Mono<UserRegistrationResponse> registerUser(Mono<UserRegistrationRequest> registrationRequest);
}
