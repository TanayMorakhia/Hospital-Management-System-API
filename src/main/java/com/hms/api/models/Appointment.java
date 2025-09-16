package com.hms.api.models;

import com.hms.api.models.AppointmentStatus;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    private String reason;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // constructors, getters, setters
}
