package com.hms.api.models;

import com.hms.api.util.CustomId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Admin {
    @Id
    private String id; // custom IDs provided manually

    private String email;

    private String password; // BCrypt hashed
}


