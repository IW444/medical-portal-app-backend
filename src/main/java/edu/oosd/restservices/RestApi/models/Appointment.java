package edu.oosd.restservices.RestApi.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Don't need this if we keep Appointment and User in the same model folder.
//import edu.oosd.restservices.RestApi.models.User;

/**
 * Entity class representing a medical appointment in the database.
 * This class maps to the appointments table and links patients and doctors
 * through a many-to-one relationship.
 */
@Entity
@Table(name = "appointments")
public class Appointment {

    /** Unique identifier for the appointment, auto-increments in the database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentId")
    private Integer appointmentId;

    /** The date the appointment is scheduled for. */
    @Column(name = "date")
    private LocalDate date;

    /** The scheduled start time of the appointment. */
    @Column(name = "startTime")
    private LocalTime startTime;

    /** The scheduled end time of the appointment. */
    @Column(name = "endTime")
    private LocalTime endTime;

    /** * The patient associated with this appointment.
     * Maps to the patientId foreign key in the appointments table.
     */
    @ManyToOne
    @JoinColumn(name = "patientId")
    private User patient;

    /** The doctor assigned to this appointment.
     * Maps to the doctorId foreign key in the appointments table.
     */
    @ManyToOne
    @JoinColumn(name = "doctorId")
    private User doctor;

    /** The timestamp of when the appointment record was created or last modified. */
    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;


    /**
     * Default no-argument constructor required.
     */
    public Appointment() {
    }

    /**
     * All argument constructor for creating a complete Appointment.
     * @param appointmentId The unique ID of the appointment.
     * @param date          The date of the appointment.
     * @param startTime     The start time of the appointment.
     * @param endTime       The end time of the appointment.
     * @param patient       The User object representing the patient.
     * @param doctor        The User object representing the doctor.
     * @param timeStamp     The creation/update timestamp.
     */
    public Appointment(Integer appointmentId,
                       LocalDate date,
                       LocalTime startTime,
                       LocalTime endTime,
                       User patient,
                       User doctor,
                       LocalDateTime timeStamp) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.patient = patient;
        this.doctor = doctor;
        this.timeStamp = timeStamp;
    }

    /** @return The unique appointment id. */
    public Integer getAppointmentId() {
        return appointmentId;
    }

    /** @param appointmentId The unique appointment id to set. */
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** @return The scheduled date of the appointment. */
    public LocalDate getDate() {
        return date;
    }

    /** @param date The scheduled date of the appointment to set. */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** @return The start time of the appointment. */
    public LocalTime getStartTime() {
        return startTime;
    }

    /** @param startTime The start time of the appointment to set. */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /** @return The end time of the appointment. */
    public LocalTime getEndTime() {
        return endTime;
    }

    /** @param endTime The end time of the appointment to set. */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /** @return The User object for the patient. */
    public User getPatient() {
        return patient;
    }

    /** @param patient The User object for the patient to set. */
    public void setPatient(User patient) {
        this.patient = patient;
    }

    /** @return The User object for the doctor. */
    public User getDoctor() {
        return doctor;
    }

    /** @param doctor The User object for the doctor to set. */
    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    /** @return The timestamp of the appointment. */
    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    /** @param timeStamp The timestamp of the appointment to set. */
    public void setTimestamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}