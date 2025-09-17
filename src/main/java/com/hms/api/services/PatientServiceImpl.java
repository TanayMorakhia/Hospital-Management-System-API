package com.hms.api.services;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.api.dto.PatientLoginDTO;
import com.hms.api.dto.PatientRegisterDTO;
import com.hms.api.dto.PatientResponseDTO;
import com.hms.api.models.Patient;
import com.hms.api.repositories.PatientRepository;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired private PatientRepository patientRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Override
    public PatientResponseDTO register(PatientRegisterDTO dto) {
        patientRepository.findByEmail(dto.getEmail()).ifPresent(p -> { throw new IllegalArgumentException("Email already registered"); });
        Patient p = new Patient();
        p.setName(dto.getName());
        p.setEmail(dto.getEmail());
        p.setContact(dto.getContact());
        p.setAddress(dto.getAddress());
        p.setGender(dto.getGender());
        p.setPassword(passwordEncoder.encode(dto.getPassword()));
        p = patientRepository.save(p);
        return toResponse(p);
    }

    @Override
    public PatientResponseDTO login(PatientLoginDTO dto) {
        Patient p = patientRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(dto.getPassword(), p.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return toResponse(p);
    }

    @Override
    public PatientResponseDTO getById(String id) {
        Patient p = patientRepository.findById(id).orElseThrow();
        return toResponse(p);
    }

    @Override
    public List<PatientResponseDTO> listAll() {
        return StreamSupport.stream(patientRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .toList();
    }

    private PatientResponseDTO toResponse(Patient p){
        return new PatientResponseDTO(p.getId(), p.getName(), p.getEmail(), p.getContact(), p.getAddress(), p.getGender());
    }
}


