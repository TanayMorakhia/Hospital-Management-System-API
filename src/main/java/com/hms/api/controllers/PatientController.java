package com.hms.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.PatientLoginDTO;
import com.hms.api.dto.PatientRegisterDTO;
import com.hms.api.dto.PatientResponseDTO;
import com.hms.api.services.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponseDTO> register(@RequestBody PatientRegisterDTO dto){
        return new ResponseEntity<>(patientService.register(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<PatientResponseDTO> login(@RequestBody PatientLoginDTO dto){
        return ResponseEntity.ok(patientService.login(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.name")
    public ResponseEntity<PatientResponseDTO> getById(@PathVariable String id){
        return ResponseEntity.ok(patientService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponseDTO>> listAll(){
        return ResponseEntity.ok(patientService.listAll());
    }
}
