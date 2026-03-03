package edu.oosd.restservices.RestApi;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(name = "patientId")
    private Integer patientId;

    @Column(name = "doctorId")
    private Integer doctorId;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    public Appointment() {}

    public Appointment {//
        private Integer appointmentId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer patientId;
        private Integer doctorId;
        private LocalDateTime timeStamp;


        //Constructor
    public Appointment(Integer appointmentId,
                LocalDate date,
                LocalTime startTime,
                LocalTime endTime,
                Integer patientId,
                Integer doctorId,
                LocalDateTime timeStamp) {
            this.appointmentId = appointmentId;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.timeStamp = timeStamp;
        }

        public Integer getAppointmentId() {
            return appointmentId;
        }

        public void setId(Integer appointmentId) {
            this.appointmentId = appointmentId;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

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

        public Integer getPatientId() {
            return patientId;
        }

        public void setPatientId(Integer patientId) {
            this.patientId = patientId;
        }

        public Integer getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Integer doctorId) {
            this.doctorId = doctorId;

        }

        public LocalDateTime getTimestamp() {
            return timeStamp;
        }

        public void setTimestamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
        }
    }