package com.craftelix.weatherviewer.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class User {

    @Id
    private Long id;

    private String username;

    private String password;
}
