package com.example.aerosaga.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMissionRequest {

    @NotNull
    private Long droneId;

    @NotNull
    private Double pickupLat;

    @NotNull
    private Double pickupLng;

    @NotNull
    private Double dropLat;

    @NotNull
    private Double dropLng;

    private Boolean forceDropFailure;
}