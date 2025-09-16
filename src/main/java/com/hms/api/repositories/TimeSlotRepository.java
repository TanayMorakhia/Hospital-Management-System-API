package com.hms.api.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.api.models.SlotStatus;
import com.hms.api.models.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDoctorIdAndSlotDateBetweenOrderBySlotDateAscStartTimeAsc(
        String doctorId, LocalDate startDate, LocalDate endDate);

    List<TimeSlot> findByDoctorIdAndSlotDateAndStatusOrderByStartTimeAsc(
        String doctorId, LocalDate date, SlotStatus status);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.doctor.id = :doctorId " +
           "AND ts.slotDate BETWEEN :startDate AND :endDate " +
           "AND ts.status = :status ORDER BY ts.slotDate, ts.startTime")
    List<TimeSlot> findAvailableSlots(@Param("doctorId") String doctorId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("status") SlotStatus status);

    boolean existsByDoctorIdAndSlotDateAndStartTime(String doctorId, LocalDate date, LocalTime startTime);
}
