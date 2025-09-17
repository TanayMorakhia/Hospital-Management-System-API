package com.hms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.api.services.DoctorAvailabilityServiceImpl;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {
    @Autowired
    private DoctorAvailabilityServiceImpl availabilityService;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        availabilityService.generateTimeSlotsForAllDoctors();
    }
}
