package com.example.RestApp.services;

import com.example.RestApp.models.Sensor;
import com.example.RestApp.repositories.SensorsRepository;
import com.example.RestApp.util.SensorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorsService {
    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository){
        this.sensorsRepository = sensorsRepository;
    }

    public List<Sensor> findAll(){
        return sensorsRepository.findAll();
    }

    public Sensor findOne(int id){
        Optional<Sensor> foundSensor = sensorsRepository.findById(id);
        return foundSensor.orElseThrow(SensorNotFoundException::new);
    }

    public Optional<Sensor> findByName(String name){
        return sensorsRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor){
        sensorsRepository.save(sensor);
    }
}
