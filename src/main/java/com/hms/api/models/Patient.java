package com.hms.api.models;

import com.hms.api.util.CustomId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Patient {

    @Id
    @CustomId
    String id;
}
