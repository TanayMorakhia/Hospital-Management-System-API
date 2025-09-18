package com.hms.api.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.DoctorCreationDTO;
import com.hms.api.dto.DoctorResponseDTO;
import com.hms.api.models.Doctor;
import com.hms.api.models.TimeSlot;
import com.hms.api.services.DoctorAvailabilityServiceImpl;
import com.hms.api.services.DoctorService;
import com.hms.api.services.DoctorServiceImpl;
import com.hms.api.repositories.DoctorRepository;
import com.hms.api.repositories.TimeSlotRepository;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private DoctorAvailabilityServiceImpl availabilityService;

    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @PostMapping
    public ResponseEntity<Doctor> saveDoctor(@RequestBody DoctorCreationDTO doctor){
        Doctor temp = doctorService.saveDoctor(doctor);
        availabilityService.generateTimeSlots(temp.getId());
        return new ResponseEntity<>(temp, HttpStatus.CREATED);
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable String doctorId){
        doctorService.deactivateDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> listDoctors(){
        List<DoctorResponseDTO> list = doctorRepository.findAll().stream().map(d ->
            new DoctorResponseDTO(
                d.getId(),
                d.getName(),
                d.getDepartment(),
                d.getGender(),
                d.getQualification(),
                d.getYearsOfExperience(),
                d.getPrice(),
                d.getStartTime(),
                d.getEndTime(),
                d.isActive()
            )
        ).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{doctorId}/available-slots")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TimeSlot> slots = availabilityService.getAvailableSlots(doctorId, startDate, endDate);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/{doctorId}/schedule/{date}")
    public ResponseEntity<List<TimeSlot>> getDaySchedule(
            @PathVariable String doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TimeSlot> slots = availabilityService.getDaySchedule(doctorId, date);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<List<TimeSlot>> getFullSchedule(@PathVariable String doctorId){
        return ResponseEntity.ok(timeSlotRepository.findByDoctorIdOrderBySlotDateAscStartTimeAsc(doctorId));
    }
}
