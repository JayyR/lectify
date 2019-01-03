package org.lectify.api.resource;

import org.lectify.api.service.user.UserManagementService;
import org.lectify.api.service.user.model.UserRegistrationResponse;
import org.lectify.api.service.user.model.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserManagementService userManagementService;

    @Autowired
    public UserResource(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping()
    public Mono<UserRegistrationResponse> registerUser(@RequestBody UserRegistrationRequest request){

       return userManagementService.registerUser(Mono.just(request));

    }
}
