package com.hms.api.models;

import static org.hibernate.annotations.CascadeType.ALL;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CascadeType;

import com.hms.api.util.CustomId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Doctor {

    @CustomId
    @Id
    private String id;

    private String name;

    private String department;

    private String gender;

    private String qualification;

    private Integer yearsOfExperience;

    private Double price;

    private boolean isActive;


    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    public Doctor(String name, String department, String gender, String qualification, Integer yearsOfExperience, Double price, LocalTime startTime, LocalTime endTime, boolean isActive){
        this.name = name;
        this.department = department;
        this.gender = gender;
        this.qualification = qualification;
        this.yearsOfExperience = yearsOfExperience;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    @OneToMany(mappedBy = "doctor", cascade = jakarta.persistence.CascadeType.ALL)
    @JsonIgnore
    private List<TimeSlot> timeSlots = new ArrayList<>();
}
