package com.hms.api.repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hms.api.models.DoctorSchedule;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorIdAndIsActiveTrue(String doctorId);

    List<DoctorSchedule> findByDoctorIdAndDayOfWeekAndIsActiveTrue(String doctorId, DayOfWeek dayOfWeek);

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId " +
           "AND ds.isActive = true ORDER BY ds.dayOfWeek")
    List<DoctorSchedule> findActiveDoctorSchedules(@Param("doctorId") String doctorId);

    @Modifying
    @Query("UPDATE DoctorSchedule ds SET ds.isActive = false WHERE ds.doctor.id = :doctorId")
    void deactivateAllSchedulesForDoctor(@Param("doctorId") String doctorId);

    boolean existsByDoctorIdAndDayOfWeekAndIsActiveTrue(String doctorId, DayOfWeek dayOfWeek);
}
