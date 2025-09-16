package com.hms.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.api.dto.DoctorCreationDTO;
import com.hms.api.models.Doctor;
import com.hms.api.repositories.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor saveDoctor(DoctorCreationDTO doctorDTO){
        Doctor doctor = new Doctor(doctorDTO.getName(), doctorDTO.getGender(), doctorDTO.getDepartment());
        return doctorRepository.save(doctor);
    }
}
