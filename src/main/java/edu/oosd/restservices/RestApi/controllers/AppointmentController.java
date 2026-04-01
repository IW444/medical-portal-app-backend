package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.Appointment;
import edu.oosd.restservices.RestApi.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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

    //For Doctors, find appointments by day
    @GetMapping("/doctor/{doctorId}/today")
    public List<Appointment> getAppointmentsForDoctorToday(@PathVariable Integer doctorId) {
        return appointmentRepository.findByDoctorUserIdAndDate(doctorId, LocalDate.now());
    }

    //For Doctors, find appointments by week in Sunday - Saturday format
    @GetMapping("/doctor/{doctorId}/week")
    public List<Appointment> getAppointmentsForDoctorThisWeek(@PathVariable Integer doctorId) {
        //The Tempora is allows us to go back to Sunday if we aren't at a Sunday or stay there if we are
        //Similarly, we push forward to the next Saturday unless we are already there.
        LocalDate start = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate end = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return appointmentRepository.findByDoctorUserIdAndDateBetween(doctorId, start, end);
    }

    //For Doctors, find appointments by month
    @GetMapping("/doctor/{doctorId}/month")
    public List<Appointment> getAppointmentsForDoctorThisMonth(@PathVariable Integer doctorId) {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return appointmentRepository.findByDoctorUserIdAndDateBetween(doctorId, start, end);
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