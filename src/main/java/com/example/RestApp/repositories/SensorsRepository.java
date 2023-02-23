package com.example.RestApp.repositories;

import com.example.RestApp.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorsRepository extends JpaRepository<Sensor, Integer> {
    public Optional<Sensor> findByName(String name);
}
