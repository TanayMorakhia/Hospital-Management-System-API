package com.hms.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hms.api.dto.PaymentCreateDTO;
import com.hms.api.dto.PaymentResponseDTO;
import com.hms.api.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> listAll(){
        return ResponseEntity.ok(paymentService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.name")
    public ResponseEntity<List<PaymentResponseDTO>> getByPatient(@PathVariable("id") String patientId){
        return ResponseEntity.ok(paymentService.getByPatient(patientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #dto.patientId == authentication.name")
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody PaymentCreateDTO dto){
        PaymentResponseDTO payment = paymentService.create(dto);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }
}
