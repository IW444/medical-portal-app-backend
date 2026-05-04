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

/**
 * REST controller for managing appointments in the scheduling system.
 *
 * <p>Provides endpoints for creating, retrieving, updating, and deleting
 * appointments. Supports filtering by doctor or patient with time ranges
 * such as today, this week, this month, past, and future appointments.
 * Conflict detection prevents double-booking for both doctors and patients.</p>
 *
 * <p>Base URL: {@code /appointments}</p>
 */

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Retrieves all appointments in the system.
     *
     * @return a list of all {@link Appointment} records
     */

    @GetMapping
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Retrieves all appointments for a specific doctor scheduled for today.
     *
     * @param doctorId the unique ID of the doctor
     * @return a list of today's {@link Appointment} records for the given doctor
     */

    //For Doctors, find appointments by day
    @GetMapping("/doctor/{doctorId}/today")
    public List<Appointment> getAppointmentsForDoctorToday(@PathVariable Integer doctorId) {
        return appointmentRepository.findByDoctorUserIdAndDate(doctorId, LocalDate.now());
    }


    /**
     * Retrieves all appointments for a specific doctor within the current week
     * (Sunday through Saturday).
     *
     * <p>Uses {@link TemporalAdjusters} to compute the boundaries: rolls back
     * to the most recent Sunday and forward to the nearest Saturday.</p>
     *
     * @param doctorId the unique ID of the doctor
     * @return a list of {@link Appointment} records for the current week
     */

    //For Doctors, find appointments by week in Sunday - Saturday format
    @GetMapping("/doctor/{doctorId}/week")
    public List<Appointment> getAppointmentsForDoctorThisWeek(@PathVariable Integer doctorId) {
        //The Tempora allows us to go back to Sunday if we aren't at a Sunday or stay there if we are
        //Similarly, we push forward to the next Saturday unless we are already there.
        LocalDate start = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate end = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return appointmentRepository.findByDoctorUserIdAndDateBetween(doctorId, start, end);
    }

    /**
     * Retrieves all appointments for a specific doctor within the current calendar month.
     *
     * @param doctorId the unique ID of the doctor
     * @return a list of {@link Appointment} records for the current month
     */

    //For Doctors, find appointments by month
    @GetMapping("/doctor/{doctorId}/month")
    public List<Appointment> getAppointmentsForDoctorThisMonth(@PathVariable Integer doctorId) {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return appointmentRepository.findByDoctorUserIdAndDateBetween(doctorId, start, end);
    }

    /**
     * Retrieves all upcoming (today and future) appointments for a specific patient.
     *
     * @param patientId the unique ID of the patient
     * @return a list of future {@link Appointment} records for the given patient
     */

    //For Patients, find future appointments
    @GetMapping("/patient/{patientId}/future")
    public List<Appointment> getAppointmentsForPatientFuture(@PathVariable Integer patientId) {
        return appointmentRepository.findByPatientUserIdAndDateGreaterThanEqual(patientId, LocalDate.now());
    }


    /**
     * Retrieves all past appointments for a specific patient (before today).
     *
     * @param patientId the unique ID of the patient
     * @return a list of past {@link Appointment} records for the given patient
     */

    //For Patients, find past appointments
    @GetMapping("/patient/{patientId}/past")
    public List<Appointment> getAppointmentsForPatientPast(@PathVariable Integer patientId) {
        return appointmentRepository.findByPatientUserIdAndDateLessThan(patientId, LocalDate.now());
    }

    /**
     * Retrieves all upcoming (today and future) appointments for a specific doctor.
     *
     * @param doctorId the unique ID of the doctor
     * @return a list of future {@link Appointment} records for the given doctor
     */

    //For Doctors, find future appointments
    @GetMapping("/doctor/{doctorId}/future")
    public List<Appointment> getAppointmentsForDoctorFuture(@PathVariable Integer doctorId) {
        return appointmentRepository.findByDoctorUserIdAndDateGreaterThanEqual(doctorId, LocalDate.now());
    }

    /**
     * Retrieves all past appointments for a specific doctor (before today).
     *
     * @param doctorId the unique ID of the doctor
     * @return a list of past {@link Appointment} records for the given doctor
     */

    //For Docotors, find past appointments
    @GetMapping("/doctor/{doctorId}/past")
    public List<Appointment> getAppointmentsForDoctorPast(@PathVariable Integer doctorId) {
        return appointmentRepository.findByDoctorUserIdAndDateLessThan(doctorId, LocalDate.now());
    }

    /**
     * Creates a new appointment after validating there are no scheduling conflicts.
     *
     * <p>Conflict checks are performed before saving: first for the doctor,
     * then for the patient. If either has an overlapping appointment, the
     * request is rejected with HTTP 409 Conflict. The creation timestamp is
     * set automatically.</p>
     *
     * @param appointment the {@link Appointment} object to create, provided in the request body
     * @return {@code 201 Created} with the saved appointment, or
     *         {@code 409 Conflict} with an error message if a scheduling clash is detected
     */

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

    /**
     * Fully replaces an existing appointment by ID after validating scheduling conflicts.
     *
     * <p>Overlap checks exclude the appointment being updated so it does not
     * conflict with its own previous time slot. Returns 404 if no appointment
     * exists with the given ID. The update timestamp is set automatically.</p>
     *
     * @param id          the ID of the appointment to update
     * @param appointment the updated {@link Appointment} data from the request body
     * @return {@code 200 OK} with the updated appointment,
     *         {@code 404 Not Found} if the appointment does not exist, or
     *         {@code 409 Conflict} if a scheduling clash is detected
     */

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

    /**
     * Partially updates an existing appointment's fields by ID.
     *
     * <p>Only non-null fields in the request body are applied to the existing
     * record. Supported fields: date, startTime, endTime, patient, and doctor.
     * Note: this method does not perform conflict checking on partial updates.</p>
     *
     * @param id      the ID of the appointment to patch
     * @param partial an {@link Appointment} object containing only the fields to update
     * @return {@code 200 OK} with the patched appointment, or
     *         {@code 404 Not Found} if the appointment does not exist
     */

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

    /**
     * Deletes an appointment by its ID.
     *
     * @param id the ID of the appointment to delete
     * @return {@code 204 No Content} on successful deletion, or
     *         {@code 404 Not Found} if no appointment exists with the given ID
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {

        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}



