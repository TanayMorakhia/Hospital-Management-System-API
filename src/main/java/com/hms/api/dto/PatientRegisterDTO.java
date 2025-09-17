package com.hms.api.dto;

import lombok.Data;

@Data
public class PatientRegisterDTO {
    private String name;
    private String email;
    private String contact;
    private String address;
    private String gender;
    private String password;
}


