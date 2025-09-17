package com.hms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.api.services.DoctorAvailabilityServiceImpl;
import com.hms.api.repositories.AdminRepository;
import com.hms.api.models.Admin;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {
    @Autowired
    private DoctorAvailabilityServiceImpl availabilityService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        availabilityService.generateTimeSlotsForAllDoctors();
        // seed admins
        if (adminRepository.findById("AD00000000").isEmpty()) {
            Admin a = new Admin();
            a.setId("AD00000000");
            a.setEmail("admin0@hms.local");
            a.setPassword(encoder.encode("superman"));
            adminRepository.save(a);
        }
        if (adminRepository.findById("AD99999999").isEmpty()) {
            Admin a = new Admin();
            a.setId("AD99999999");
            a.setEmail("admin9@hms.local");
            a.setPassword(encoder.encode("superman"));
            adminRepository.save(a);
        }
    }
}
