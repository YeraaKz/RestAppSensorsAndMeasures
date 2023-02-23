package com.example.RestApp.util;

import com.example.RestApp.models.Measure;
import com.example.RestApp.services.SensorsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasureValidator implements Validator {

    private final SensorsService sensorsService;
    public MeasureValidator(SensorsService sensorsService){
        this.sensorsService = sensorsService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Measure.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measure measure = (Measure) target;
        if(measure.getSensor() == null){
            return;
        }

        if(sensorsService.findByName(measure.getSensor().getName()).isEmpty()){
            errors.rejectValue("sensor", "", "There is no sensor with that name");
        }
    }
}
