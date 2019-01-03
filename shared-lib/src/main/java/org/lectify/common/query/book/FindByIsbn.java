package org.lectify.common.query.book;

import lombok.Value;

@Value
public class FindByIsbn {

    String type;

    String isbn;
}
