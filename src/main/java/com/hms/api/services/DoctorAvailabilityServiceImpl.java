package com.hms.api.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.api.models.Doctor;
import com.hms.api.models.SlotStatus;
import com.hms.api.models.TimeSlot;
import com.hms.api.repositories.TimeSlotRepository;
import com.hms.api.repositories.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@Transactional
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private static final int SLOT_DURATION_MINUTES = 30;
    private static final int FUTURE_DAYS_TO_GENERATE = 60; // 3 months ahead

    /**
     * Generate time slots for a doctor based on their schedule
     */
    public void generateTimeSlots(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(FUTURE_DAYS_TO_GENERATE);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            createTimeSlotsForDay(doctor, date);
        }
    }

    public void generateTimeSlotsForAllDoctors() {
        doctorRepository.findAll().forEach(doc -> generateTimeSlots(doc.getId()));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailySlotGeneration() {
        generateTimeSlotsForAllDoctors();
    }

    /**
     * Create time slots for a specific day
     */
    private void createTimeSlotsForDay(Doctor doctor, LocalDate date) {
        LocalTime currentTime = doctor.getStartTime();
        LocalTime endTime = doctor.getEndTime();

        while (currentTime.isBefore(endTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(SLOT_DURATION_MINUTES);

            // Don't create slot if it exceeds working hours
            if (slotEndTime.isAfter(endTime)) {
                break;
            }

            // Check if slot already exists
            if (!timeSlotRepository.existsByDoctorIdAndSlotDateAndStartTime(doctor.getId(), date, currentTime)) {
                TimeSlot slot = new TimeSlot();
                slot.setDoctor(doctor);
                slot.setSlotDate(date);
                slot.setStartTime(currentTime);
                slot.setEndTime(slotEndTime);
                slot.setStatus(SlotStatus.AVAILABLE);

                timeSlotRepository.save(slot);
            }

            currentTime = slotEndTime;
        }
    }

    /**
     * Get available slots for a doctor within date range
     */
    public List<TimeSlot> getAvailableSlots(String doctorId, LocalDate startDate, LocalDate endDate) {
        return timeSlotRepository.findAvailableSlots(doctorId, startDate, endDate, SlotStatus.AVAILABLE);
    }

    /**
     * Get all slots for a doctor on a specific date
     */
    public List<TimeSlot> getDaySchedule(String doctorId, LocalDate date) {
        return timeSlotRepository.findByDoctorIdAndSlotDateAndStatusOrderByStartTimeAsc(
            doctorId, date, SlotStatus.AVAILABLE);
    }

    /**
     * Book a time slot
     */
    public boolean bookTimeSlot(Long timeSlotId) {
        Optional<TimeSlot> slotOpt = timeSlotRepository.findById(timeSlotId);

        if (slotOpt.isPresent() && slotOpt.get().getStatus() == SlotStatus.AVAILABLE) {
            TimeSlot slot = slotOpt.get();
            if (slot.getSlotDate().isAfter(LocalDate.now().plusDays(FUTURE_DAYS_TO_GENERATE))) {
                throw new IllegalArgumentException("Cannot book beyond 60 days from today");
            }
            slot.setStatus(SlotStatus.BOOKED);
            timeSlotRepository.save(slot);
            return true;
        }
        return false;
    }

}
