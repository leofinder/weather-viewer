package com.craftelix.weatherviewer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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

    @Column("user_id")
    private Long userId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String country;

    private String state;

}
