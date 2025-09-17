package com.hms.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethodEnum;

    private boolean isSuccess;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
