package com.hms.api.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private String id;
    private String name;
    private String department;
    private String gender;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean active;
}


