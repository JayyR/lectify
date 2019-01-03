package org.lectify.query.handler.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.lectify.common.query.user.FindUserByEmail;
import org.lectify.common.value.book.LectifyUser;
import org.lectify.query.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UserQueryHandler {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserQueryHandler(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @QueryHandler
    public LectifyUser handle(FindUserByEmail findUserByEmail){
        String emailId = findUserByEmail.getEmailId();
        return new LectifyUser(UUID.randomUUID(),"janaka.n.ranathunga@");
        /*return userRepository.findByEmailId(emailId)
                .map(user -> modelMapper.map(user,LectifyUser.class))
                .log()
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    log.error(throwable.getMessage());
                    return Mono.empty();
                }).block();*/
    }
}
