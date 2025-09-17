package com.hms.api.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.api.dto.AppointmentCreateDTO;
import com.hms.api.models.Appointment;
import com.hms.api.models.AppointmentStatus;
import com.hms.api.models.Doctor;
import com.hms.api.models.Patient;
import com.hms.api.models.SlotStatus;
import com.hms.api.models.TimeSlot;
import com.hms.api.repositories.AppointmentRepository;
import com.hms.api.repositories.DoctorRepository;
import com.hms.api.repositories.PatientRepository;
import com.hms.api.repositories.TimeSlotRepository;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    private static final int FUTURE_DAYS_TO_GENERATE = 60;

    @Override
    public List<Appointment> listAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment create(AppointmentCreateDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();
        TimeSlot slot = timeSlotRepository.findById(dto.getTimeSlotId()).orElseThrow();

        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("TimeSlot does not belong to the doctor");
        }
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("TimeSlot is not available");
        }
        if (slot.getSlotDate().isAfter(LocalDate.now().plusDays(FUTURE_DAYS_TO_GENERATE))) {
            throw new IllegalArgumentException("Cannot book beyond 60 days from today");
        }

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setTimeSlot(slot);
        appt.setAppointmentDate(slot.getSlotDate());
        appt.setReason(dto.getReason());
        appt.setDescription(dto.getDescription());
        appt.setStatus(AppointmentStatus.SCHEDULED);

        slot.setStatus(SlotStatus.BOOKED);
        timeSlotRepository.save(slot);

        return appointmentRepository.save(appt);
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentRepository.findById(id).orElseThrow();
    }

    @Override
    public Appointment cancel(Long id) {
        Appointment appt = appointmentRepository.findById(id).orElseThrow();
        appt.setStatus(AppointmentStatus.CANCELLED);
        TimeSlot slot = appt.getTimeSlot();
        if (slot != null) {
            slot.setStatus(SlotStatus.AVAILABLE);
            timeSlotRepository.save(slot);
        }
        return appointmentRepository.save(appt);
    }

    @Override
    public List<Appointment> getByPatient(String patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateAsc(patientId);
    }
}


