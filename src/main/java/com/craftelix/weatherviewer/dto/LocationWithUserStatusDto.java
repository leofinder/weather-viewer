package com.craftelix.weatherviewer.dto;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationWithUserStatusDto {

    private LocationApiDto location;

    private boolean isAdded;
}
