package com.hms.api.models;

import static org.hibernate.annotations.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CascadeType;

import com.hms.api.util.CustomId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @CustomId
    @Id
    String id;

    String name;

    String department;

    String gender;

    public Doctor(String name, String department, String gender){
        this.name = name;
        this.department = department;
        this.gender = gender;
    }

    @OneToMany(mappedBy = "doctor", cascade = jakarta.persistence.CascadeType.ALL)
    private List<DoctorSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = jakarta.persistence.CascadeType.ALL)
    private List<TimeSlot> timeSlots = new ArrayList<>();
}
