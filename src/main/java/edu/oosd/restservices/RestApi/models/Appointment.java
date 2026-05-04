package edu.oosd.restservices.RestApi.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Don't need this if we keep Appointment and User in the same model folder.
//import edu.oosd.restservices.RestApi.models.User;


@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentId")
    private Integer appointmentId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private User doctor;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;


    public Appointment() {}

    //Constructor
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

    public Integer getAppointmentId() { return appointmentId;}

    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId;}

    public LocalDate getDate() { return date;}

    public void setDate(LocalDate date) { this.date = date;}

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getTimestamp() {return timeStamp;}

    public void setTimestamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}