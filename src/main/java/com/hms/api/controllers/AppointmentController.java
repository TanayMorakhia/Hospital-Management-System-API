package com.hms.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.AppointmentCreateDTO;
import com.hms.api.models.Appointment;
import com.hms.api.services.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> listAll(){
        return ResponseEntity.ok(appointmentService.listAll());
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody AppointmentCreateDTO dto){
        Appointment a = appointmentService.create(dto);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Appointment> cancel(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.cancel(id));
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Appointment>> getByPatient(@PathVariable("id") String patientId){
        return ResponseEntity.ok(appointmentService.getByPatient(patientId));
    }
}


