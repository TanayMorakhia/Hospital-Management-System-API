package com.hms.api.services;

import java.util.List;

import com.hms.api.dto.AppointmentCreateDTO;
import com.hms.api.models.Appointment;

public interface AppointmentService {
    List<Appointment> listAll();
    Appointment create(AppointmentCreateDTO dto);
    Appointment getById(Long id);
    Appointment cancel(Long id);
    List<Appointment> getByPatient(String patientId);
}


