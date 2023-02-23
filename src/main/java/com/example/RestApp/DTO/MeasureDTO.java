package com.example.RestApp.DTO;

import com.example.RestApp.models.Sensor;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasureDTO {
    @Min(-100)
    @Max(100)
    @NotNull
    private Float value;

    @NotNull
    private Boolean raining;

    private SensorDTO sensor;
}
