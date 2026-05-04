package edu.oosd.restservices.RestApi.models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentTest {

    private static User newGuy2;
    private static User newDoctor;
    private static Appointment newAppointment;


    @BeforeEach
    public void loadTest(){
//--------------------------
//Mock Patient for test usage
//--------------------------
        newGuy2 = new User(151,"Jreg","Jroog", "JJroog1", "password",
                Role.PATIENT, LocalDateTime.of(2026,2,10,2,36),
                LocalDateTime.of(2026,2,10,2,50));
//--------------------------
//Mock Doctor for test usage
//--------------------------
        newDoctor = new User(5,"Alyssa","Doctorson", "A.Doctor",
                "MedSchool321",
                Role.DOCTOR, LocalDateTime.of(2026,1,6,5,45),
                LocalDateTime.of(2025,11,20,1,30));
//--------------------------
//Mock Appointment for test usage
//--------------------------
        newAppointment = new Appointment(35, LocalDate.of(2027,5,20),
                LocalTime.of(14, 30, 0), LocalTime.of(15,30,0),
                newGuy2,newDoctor, LocalDateTime.of(2027,3,1,12,0,0));
    }

//--------------------------
//GETTER Tests
//--------------------------
    @Test
    void getAppointmentId() {
        //Verify that the Appointment ID matches the correct Appointment.
        assertEquals(35, newAppointment.getAppointmentId(), "Please verify correct Appointment ID.");
    }

    @Test
    void getDate() {
        //Verify that the set Appointment date matches the correct Appointment.
        assertEquals(LocalDate.of(2027,5,20),
                newAppointment.getDate(), "Please verify correct Appointment date.");
    }

    @Test
    void getStartTime() {
        //Verify that the set Appointment Start Time matches the correct Appointment.
        assertEquals(LocalTime.of(14, 30, 0),
                newAppointment.getStartTime(), "Please verify correct Appointment start time.");
    }

    @Test
    void getEndTime() {
        //Verify that the set Appointment End Time matches the correct Appointment.
        assertEquals(LocalTime.of(15, 30, 0),
                newAppointment.getEndTime(), "Please verify correct Appointment end time.");
    }

    @Test
    void getPatient() {
        //Verify that the correct patient is assigned to the appointment.
        assertEquals(newGuy2,newAppointment.getPatient(), "Please verify that this Appointment is for the correct patient.");
    }

    @Test
    void getDoctor() {
        //Verify that the correct doctor is assigned to the appointment.
        assertEquals(newDoctor, newAppointment.getDoctor(), "Please verify that the correct doctor is assigned to this appointment.");
    }

    @Test
    void getTimestamp(){
        assertEquals(LocalDateTime.of(2027,3,1,12,0,0),
                newAppointment.getTimestamp(),"Please verify that the correct timestamp is applied.");
    }


//--------------------------
//SETTER tests
//--------------------------

    @Test
    void setDate() {
        //Verify that the appointment is set to the correct date
        newAppointment.setDate(LocalDate.of(2027,6,25));
        assertEquals(LocalDate.of(2027,6,25), newAppointment.getDate(),
                "Incorrect Date assigned to appointment");
   }

    @Test
    void setStartTime() {
        //Verify that the appointment is set to start at the correct time
        newAppointment.setStartTime(LocalTime.of(12, 15, 0));
        assertEquals(LocalTime.of(12, 15, 0), newAppointment.getStartTime(),
                "Incorrect Start time assigned to appointment");
    }

    @Test
    void setEndTime() {
        //Verify that the appointment is set to end at the correct time
        newAppointment.setEndTime(LocalTime.of(13, 15, 0));
        assertEquals(LocalTime.of(13, 15, 0), newAppointment.getEndTime(),
                "Incorrect End time assigned to appointment");
    }

    @Test
    void setPatient() {
        //Verify that patients are assigned to appointments correctly.
        newAppointment.setPatient(newGuy2);
        assertEquals(newGuy2, newAppointment.getPatient(), "Incorrect patient assigned to appointment");
    }

    @Test
    void setDoctor() {
        //Verify that the doctor is assigned to appointments correctly.
        User newDoctorAgain = new User(5,"Sarah","Miller", "S.Miller", "Med456",
                Role.DOCTOR, LocalDateTime.of(2026,1,6,5,45),
                LocalDateTime.of(2025,11,20,1,30));
        newAppointment.setDoctor(newDoctorAgain);
        assertEquals(newDoctorAgain, newAppointment.getDoctor(), "Incorrect doctor assigned to appointment");
    }

}