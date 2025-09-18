package com.hms.api.services;

import java.util.List;

import com.hms.api.dto.PaymentCreateDTO;
import com.hms.api.dto.PaymentResponseDTO;
import com.hms.api.models.Payment;

public interface PaymentService {
    List<PaymentResponseDTO> listAll();
    PaymentResponseDTO getById(Long id);
    List<PaymentResponseDTO> getByPatient(String patientId);
    PaymentResponseDTO create(PaymentCreateDTO dto);
}
