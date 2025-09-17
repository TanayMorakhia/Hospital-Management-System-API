package com.hms.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hms.api.models.Patient;

@Repository
public interface PatientRepository extends CrudRepository<Patient, String> {
    Optional<Patient> findByEmail(String email);
}
