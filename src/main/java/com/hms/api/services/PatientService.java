package com.hms.api.services;

import java.util.List;

import com.hms.api.dto.PatientLoginDTO;
import com.hms.api.dto.PatientRegisterDTO;
import com.hms.api.dto.PatientResponseDTO;

public interface PatientService {
    PatientResponseDTO register(PatientRegisterDTO dto);
    PatientResponseDTO login(PatientLoginDTO dto);
    PatientResponseDTO getById(String id);
    List<PatientResponseDTO> listAll();
}


