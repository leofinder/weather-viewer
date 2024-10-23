package com.craftelix.weatherviewer.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("locations")
public class Location {

    @Id
    private Long id;

    private String name;

    private User user;

    private BigDecimal latitude;

    private BigDecimal longitude;
}
