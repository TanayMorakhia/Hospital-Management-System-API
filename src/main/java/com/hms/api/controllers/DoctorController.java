package com.hms.api.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.DoctorCreationDTO;
import com.hms.api.models.Doctor;
import com.hms.api.models.DoctorSchedule;
import com.hms.api.models.TimeSlot;
import com.hms.api.services.DoctorAvailabilityServiceImpl;
import com.hms.api.services.DoctorService;
import com.hms.api.services.DoctorServiceImpl;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private DoctorAvailabilityServiceImpl availabilityService;

    @Autowired
    private DoctorServiceImpl doctorService;

    @PostMapping
    public ResponseEntity<Doctor> saveDoctor(@RequestBody DoctorCreationDTO doctor){
        Doctor temp = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(temp, HttpStatus.CREATED);
    }

    @PostMapping("/{doctorId}/generate-slots")
    public ResponseEntity<String> generateTimeSlots(@PathVariable String doctorId) {
        availabilityService.generateTimeSlots(doctorId);
        return ResponseEntity.ok("Time slots generated successfully");
    }

    @GetMapping("/{doctorId}/available-slots")
    public ResponseEntity<List<TimeSlot>> getAvailableSlots(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TimeSlot> slots = availabilityService.getAvailableSlots(doctorId, startDate, endDate);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/{doctorId}/day-schedule/{date}")
    public ResponseEntity<List<TimeSlot>> getDaySchedule(
            @PathVariable String doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TimeSlot> slots = availabilityService.getDaySchedule(doctorId, date);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/{doctorId}/schedule")
    public ResponseEntity<String> updateSchedule(
            @PathVariable String doctorId,
            @RequestBody List<DoctorSchedule> schedules) {

        availabilityService.updateDoctorSchedule(doctorId, schedules);
        return ResponseEntity.ok("Schedule updated successfully");
    }
}
