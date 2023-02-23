package com.example.RestApp.util;

import com.example.RestApp.models.Sensor;
import com.example.RestApp.services.SensorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {
    private final SensorsService sensorsService;

    @Autowired
    public SensorValidator(SensorsService sensorsService){
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;
        // Check if there is a sensor with similar name
        if(sensorsService.findByName(((Sensor) target).getName()).isPresent()){
            errors.rejectValue("name", "", "Sensor with this name is already exists");
        }
    }
}
