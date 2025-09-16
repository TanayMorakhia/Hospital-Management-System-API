package com.hms.api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "time_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id", "start_time"})
})
@Data
@NoArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "slot_date")
    private LocalDate slotDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @OneToOne(mappedBy = "timeSlot")
    private Appointment appointment;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
