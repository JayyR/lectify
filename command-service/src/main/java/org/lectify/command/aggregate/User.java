package org.lectify.command.aggregate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.Repository;
import org.axonframework.spring.stereotype.Aggregate;
import org.lectify.common.command.CreateUserCommand;
import org.lectify.common.event.UserCreatedEvent;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Slf4j
public class User {

    @AggregateIdentifier
    private UUID userId;

    private String emailId;

    Repository<User> userRepository;

    @CommandHandler
    public User(CreateUserCommand createUserCommand){
        UUID userId = createUserCommand.getUserId();
        String emailId = createUserCommand.getEmailId();
        apply(new UserCreatedEvent(userId,emailId));
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent userCreatedEvent){
        this.userId = userCreatedEvent.getUserId();
        this.emailId = userCreatedEvent.getEmailId();
    }
}
