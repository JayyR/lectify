package org.lectify.common.value.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LectifyUser {

    private UUID userId;

    private String emailId;
}
