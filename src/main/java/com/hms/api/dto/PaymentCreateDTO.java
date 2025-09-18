package com.hms.api.dto;

import com.hms.api.models.PaymentMethodEnum;
import lombok.Data;

@Data
public class PaymentCreateDTO {
    private double amount;
    private PaymentMethodEnum paymentMethodEnum;
    private String patientId;
}
