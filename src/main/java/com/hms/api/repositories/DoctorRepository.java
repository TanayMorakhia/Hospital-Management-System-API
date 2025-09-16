package com.hms.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.api.models.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
}
