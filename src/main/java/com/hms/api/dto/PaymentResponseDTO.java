package com.hms.api.dto;

import com.hms.api.models.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private double amount;
    private PaymentMethodEnum paymentMethodEnum;
    private boolean isSuccess;
    private String patientId;
    private String patientName;
}
