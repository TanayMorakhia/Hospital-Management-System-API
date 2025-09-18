package com.hms.api.services;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.api.dto.PaymentCreateDTO;
import com.hms.api.dto.PaymentResponseDTO;
import com.hms.api.models.Payment;
import com.hms.api.models.Patient;
import com.hms.api.repositories.PaymentRepository;
import com.hms.api.repositories.PatientRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PatientRepository patientRepository;

    @Override
    public List<PaymentResponseDTO> listAll() {
        return StreamSupport.stream(paymentRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentResponseDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        return toResponse(payment);
    }

    @Override
    public List<PaymentResponseDTO> getByPatient(String patientId) {
        return paymentRepository.findByPatientIdOrderByIdDesc(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentResponseDTO create(PaymentCreateDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();
        
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethodEnum(dto.getPaymentMethodEnum());
        payment.setPatient(patient);
        payment.setSuccess(true); // For now, assume all payments are successful
        
        payment = paymentRepository.save(payment);
        return toResponse(payment);
    }

    private PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getAmount(),
            payment.getPaymentMethodEnum(),
            payment.isSuccess(),
            payment.getPatient().getId(),
            payment.getPatient().getName()
        );
    }
}
