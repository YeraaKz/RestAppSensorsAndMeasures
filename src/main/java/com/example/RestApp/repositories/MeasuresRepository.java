package com.example.RestApp.repositories;

import com.example.RestApp.models.Measure;
import com.example.RestApp.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasuresRepository extends JpaRepository<Measure, Integer> {

}
