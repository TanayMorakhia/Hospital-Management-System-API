package com.hms.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.PatientLoginDTO;
import com.hms.api.models.Admin;
import com.hms.api.models.Patient;
import com.hms.api.repositories.AdminRepository;
import com.hms.api.repositories.PatientRepository;
import com.hms.api.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AdminRepository adminRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private BCryptPasswordEncoder encoder;

    private final JwtUtil jwt = new JwtUtil();

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody PatientLoginDTO dto){
        Map<String, Object> res = new HashMap<>();

        Admin admin = adminRepository.findByEmail(dto.getEmail()).orElse(null);
        if (admin != null && encoder.matches(dto.getPassword(), admin.getPassword())){
            String token = jwt.generateToken(admin.getId(), "ADMIN", Map.of("email", admin.getEmail()));
            res.put("role", "ADMIN");
            res.put("id", admin.getId());
            res.put("token", token);
            return ResponseEntity.ok(res);
        }

        Patient patient = patientRepository.findByEmail(dto.getEmail()).orElse(null);
        if (patient != null && encoder.matches(dto.getPassword(), patient.getPassword())){
            String token = jwt.generateToken(patient.getId(), "PATIENT", Map.of("email", patient.getEmail()));
            res.put("role", "PATIENT");
            res.put("id", patient.getId());
            res.put("token", token);
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.status(401).build();
    }
}


