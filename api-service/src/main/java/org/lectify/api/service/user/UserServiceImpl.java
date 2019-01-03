package org.lectify.api.service.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.lectify.common.command.CreateUserCommand;
import org.lectify.common.query.user.FindUserByEmail;
import org.lectify.common.value.book.LectifyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    @Autowired
    public UserServiceImpl(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }


    @Override
    public Mono<LectifyUser> getLectifyUser(UUID uuid) {
        return Mono.empty();
    }

    @Override
    public Mono<LectifyUser> getLectifyUser(String emailId) {
        FindUserByEmail findUserByEmail = new FindUserByEmail(emailId);
        return Mono.fromFuture(
                queryGateway.query(findUserByEmail, ResponseTypes.instanceOf(LectifyUser.class)))
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Mono<LectifyUser> createLectifyUser(String emailId) {
        UUID uuid = UUID.randomUUID();
        CreateUserCommand createUserCommand = new CreateUserCommand(uuid,emailId);
        CompletableFuture<UUID> completableFuture = commandGateway.send(createUserCommand);

        return Mono.fromFuture(completableFuture)
                .map(command->new LectifyUser(command,emailId))
                .onErrorResume(Mono::error);
    }
}
