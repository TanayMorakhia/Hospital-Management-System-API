package com.hms.api.dto;

import lombok.Data;

@Data
public class AppointmentCreateDTO {
    private String doctorId;
    private String patientId;
    private Long timeSlotId;
    private String reason;
    private String description;
}


