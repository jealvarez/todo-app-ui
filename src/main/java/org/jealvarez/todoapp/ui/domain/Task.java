package org.jealvarez.todoapp.ui.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Task {

    private final Long id;
    private final String name;
    private final String description;

}
