package com.example.RestApp.controllers;

import com.example.RestApp.DTO.SensorDTO;
import com.example.RestApp.models.Sensor;
import com.example.RestApp.services.SensorsService;
import com.example.RestApp.util.SensorErrorResponse;
import com.example.RestApp.util.SensorNotCreatedException;
import com.example.RestApp.util.SensorNotFoundException;
import com.example.RestApp.util.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorsService sensorsService, SensorValidator sensorValidator, ModelMapper modelMapper){
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<SensorDTO> getAll(){
        return sensorsService.findAll().stream().map(this::convertToSensorDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDTO getOne(@PathVariable("id") int id){
        return convertToSensorDTO(sensorsService.findOne(id));                          // 200 STATUS
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e){
        SensorErrorResponse response = new SensorErrorResponse(
                "Sensor with this id was not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND -> 404 STATUS !
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult){
        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);
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
            throw new SensorNotCreatedException(errorMessage.toString()); // Throw our message with created Exception !
        }
        sensorsService.save(sensor);
        // Sending http response with an empty body and with status 200(OK)
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e){
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(), // Our created message
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // // BAD_REQUEST STATUS
    }

    private Sensor convertToSensor(SensorDTO sensorDTO){
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        return sensor;
    }

    private SensorDTO convertToSensorDTO(Sensor sensor){
        SensorDTO sensorDTO = modelMapper.map(sensor, SensorDTO.class);
        return sensorDTO;
    }


}
