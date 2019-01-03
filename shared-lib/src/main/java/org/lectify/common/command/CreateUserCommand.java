package org.lectify.common.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand {

    @TargetAggregateIdentifier
    private UUID userId;

    private String emailId;
}
