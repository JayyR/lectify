package org.lectify.api.service.user;

import lombok.extern.slf4j.Slf4j;
import org.lectify.api.service.user.model.UserRegistrationRequest;
import org.lectify.api.service.user.model.UserRegistrationResponse;
import org.lectify.common.value.book.LectifyUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserManagementServiceImpl(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<UserRegistrationResponse> registerUser(Mono<UserRegistrationRequest> registrationRequest) {


        Mono<LectifyUser> newLectifyUser = registrationRequest
                .flatMap(request -> userService.createLectifyUser(request.getEmailId()));

        return registrationRequest
                .flatMap(request -> userService.getLectifyUser(request.getEmailId()))
                .onErrorResume(Mono::error)
                .map(lectifyUser -> {throw new IllegalArgumentException();})
                .switchIfEmpty(newLectifyUser)
                .map(user->modelMapper.map(user,UserRegistrationResponse.class));

    }
}
