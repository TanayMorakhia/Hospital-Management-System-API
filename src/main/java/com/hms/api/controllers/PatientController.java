package com.hms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hms.api.repositories.PatientRepository;
import com.hms.api.models.Patient;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public Patient savePatients(@RequestBody Patient patient){
        Patient temp = patientRepository.save(patient);
        return temp;
    }
}
