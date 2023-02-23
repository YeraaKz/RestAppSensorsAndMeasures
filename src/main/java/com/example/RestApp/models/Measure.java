package com.example.RestApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "measure")
@RequiredArgsConstructor
@Data
public class Measure {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    @Min(-100)
    @Max(100)
    @NotNull
    private Float value;

    @Column(name = "raining")
    @NotNull
    private Boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    @Column(name = "measuredat")
    private LocalDateTime measuredAt;

    public Boolean isRaining() {
        return raining;
    }
    public void setRaining(Boolean raining) {
        this.raining = raining;
    }
}
