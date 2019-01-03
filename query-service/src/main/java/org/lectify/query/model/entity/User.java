package org.lectify.query.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "User")
@Data
public class User {

    @Id
    private UUID userId;

    @Indexed(unique = true)
    private String emailId;
}
