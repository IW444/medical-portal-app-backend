package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.Appointment;
import edu.oosd.restservices.RestApi.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    // Correct — check FIRST, save LAST
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {

        // 1. Check doctor clash FIRST
        boolean doctorClash = appointmentRepository.doctorOverlap(
                appointment.getDoctor().getUserId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        if (doctorClash) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Doctor already has an appointment at this time.");
        }

        // 2. Check patient clash FIRST
        boolean patientClash = appointmentRepository.patientOverlap(
                appointment.getPatient().getUserId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        if (patientClash) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Patient already has an appointment at this time.");
        }

        // 3. ONLY reach here if no clashes — now save
        appointment.setTimestamp(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer id,
                                               @RequestBody Appointment appointment) {

        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Check doctor clash — exclude current appointment being edited
        boolean doctorClash = appointmentRepository.doctorOverlapExcluding(
                appointment.getDoctor().getUserId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                id  // exclude this appointment's own ID
        );
        if (doctorClash) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Doctor already has an appointment at this time.");
        }

        //  Check patient clash — exclude current appointment being edited
        boolean patientClash = appointmentRepository.patientOverlapExcluding(
                appointment.getPatient().getUserId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                id  // exclude this appointment's own ID
        );
        if (patientClash) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Patient already has an appointment at this time.");
        }

        // Only save if no clashes
        appointment.setAppointmentId(id);
        appointment.setTimestamp(LocalDateTime.now());
        Appointment updated = appointmentRepository.save(appointment);

        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointmentField(@PathVariable Integer id,
                                                              @RequestBody Appointment partial) {

        Appointment existing = appointmentRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (partial.getDate() != null) {
            existing.setDate(partial.getDate());
        }
        if (partial.getStartTime() != null) {
            existing.setStartTime(partial.getStartTime());
        }
        if (partial.getEndTime() != null) {
            existing.setEndTime(partial.getEndTime());
        }
        if (partial.getPatient() != null) {
            existing.setPatient(partial.getPatient());
        }
        if (partial.getDoctor() != null) {
            existing.setDoctor(partial.getDoctor());
        }

        existing.setTimestamp(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {

        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}



