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

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        appointment.setTimestamp(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Integer id,
                                                         @RequestBody Appointment updatedAppointment) {

        Appointment existing = appointmentRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.setDate(updatedAppointment.getDate());
        existing.setStartTime(updatedAppointment.getStartTime());
        existing.setEndTime(updatedAppointment.getEndTime());
        existing.setPatient(updatedAppointment.getPatient());
        existing.setDoctor(updatedAppointment.getDoctor());
        existing.setTimestamp(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(existing);
        return ResponseEntity.ok(saved);
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