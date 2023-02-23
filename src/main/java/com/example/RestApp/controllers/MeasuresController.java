package com.example.RestApp.controllers;

import com.example.RestApp.DTO.MeasureDTO;
import com.example.RestApp.DTO.SensorDTO;
import com.example.RestApp.models.Measure;
import com.example.RestApp.models.Sensor;
import com.example.RestApp.services.MeasuresService;
import com.example.RestApp.services.SensorsService;
import com.example.RestApp.util.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measures")
public class MeasuresController {
    private final MeasuresService measuresService;
    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final MeasureValidator measureValidator;

    @Autowired
    public MeasuresController(MeasuresService measuresService, ModelMapper modelMapper, SensorsService sensorsService,
                              MeasureValidator measureValidator){
        this.measuresService = measuresService;
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
        this.measureValidator = measureValidator;
    }

    @GetMapping()
    public List<MeasureDTO> getAll(){
        return measuresService.findAll().stream().map(this::convertToMeasureDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MeasureDTO getOne(@PathVariable("id") int id){
        return convertToMeasureDTO(measuresService.findOne(id));
    }

    @ExceptionHandler
    private ResponseEntity<MeasureErrorResponse> handleException(MeasureNotFoundException e){
        MeasureErrorResponse response = new MeasureErrorResponse(
                "Measure with this id was not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasureDTO measureDTO,
                                             BindingResult bindingResult){
        SensorDTO sensorDTO = measureDTO.getSensor();
        Sensor sensor = sensorsService.findByName(sensorDTO.getName()).orElse(null);
        Measure measure = convertToMeasure(measureDTO);
        measureValidator.validate(measure, bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            // Creating error message if our BindingResult has any errors !
            for(FieldError error : errors){
                errorMessage.append(error.getField())
                        .append("-")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new MeasureNotCreatedException(errorMessage.toString());
        }
        measure.setSensor(sensor);
        measuresService.save(measure);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e){
        SensorErrorResponse response = new SensorErrorResponse(
                "Sensor with this name was not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND -> 404 STATUS !
    }

    @ExceptionHandler
    private ResponseEntity<MeasureErrorResponse> handleException(MeasureNotCreatedException e){
        MeasureErrorResponse response = new MeasureErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/rainyDaysCount")
    public Map<String, Integer> rainyDaysCount(){
        Integer count = 0;
        List<Measure> measures = measuresService.findAll();
        for(Measure measure : measures){
            if(measure.isRaining()){
                count++;
            }
        }
        Map<String, Integer> respone = new HashMap<>();
        respone.put("Rainy Days Count", count);
        return respone;
    }

    private Measure convertToMeasure(MeasureDTO measureDTO){
        Measure measure = modelMapper.map(measureDTO, Measure.class);
        return measure;
    }

    private MeasureDTO convertToMeasureDTO(Measure measure){
        MeasureDTO measureDTO = modelMapper.map(measure, MeasureDTO.class);
        return measureDTO;
    }
}
