package com.example.RestApp.services;

import com.example.RestApp.models.Measure;
import com.example.RestApp.models.Sensor;
import com.example.RestApp.repositories.MeasuresRepository;
import com.example.RestApp.util.MeasureNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasuresService {

    private final MeasuresRepository measuresRepository;

    @Autowired
    public MeasuresService(MeasuresRepository measuresRepository){
        this.measuresRepository = measuresRepository;
    }

    public List<Measure> findAll(){
        return measuresRepository.findAll();
    }

    public Measure findOne(int id){
        Optional<Measure> foundMeasure = measuresRepository.findById(id);
        return foundMeasure.orElseThrow(MeasureNotFoundException::new);
    }


    @Transactional
    public void save(Measure measure){
        enrichMeasure(measure);
        measuresRepository.save(measure);
    }

    private void enrichMeasure(Measure measure){
        measure.setMeasuredAt(LocalDateTime.now());
    }
}
