package edu.oosd.restservices.RestApi.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.models.Appointment;
import edu.oosd.restservices.RestApi.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Testing Appointment Model Methods")
    void testAppointmentModelMethods() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setDate(LocalDate.of(2026, 4, 1));
        appointment.setStartTime(LocalTime.of(9, 0));
        appointment.setEndTime(LocalTime.of(10, 0));

        assertEquals(1, appointment.getAppointmentId());
        assertEquals(LocalDate.of(2026, 4, 1), appointment.getDate());
        assertEquals(LocalTime.of(9, 0), appointment.getStartTime());
        assertEquals(LocalTime.of(10, 0), appointment.getEndTime());
    }


    @Test
    @DisplayName("Get Appointments Should Return All Appointments")
    void getUsers_ReturnAllAppointments() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);
        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentId(2);

        // Return list
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment1, appointment2));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].appointmentId").value(1));
    }


    @Test
    void getAppointmentsForDoctorToday() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);

        appointment1.setDoctor(doctor);


        // Return list of appropriate appointments for any chosen doctor and date

        when(appointmentRepository.findByDoctorUserIdAndDate(any(Integer.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
            mockMvc.perform(get("/appointments/doctor/1/today"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }



    @Test
    void getAppointmentsForDoctorThisWeek() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);

        appointment1.setDoctor(doctor);


        // Return list of appropriate appointments for any chosen doctor and week

        when(appointmentRepository.findByDoctorUserIdAndDateBetween(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/doctor/1/week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForDoctorThisMonth() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);

        appointment1.setDoctor(doctor);

        // Return list of appropriate appointments for any chosen doctor and month

        when(appointmentRepository.findByDoctorUserIdAndDateBetween(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/doctor/1/month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForPatientFuture() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User patient = new User();
        patient.setUserId(2);

        appointment1.setPatient(patient);

        // Return list of appropriate appointments for any chosen patient in the future

        when(appointmentRepository.findByPatientUserIdAndDateGreaterThanEqual(any(Integer.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/patient/1/future"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForPatientPast() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User patient = new User();
        patient.setUserId(2);

        appointment1.setPatient(patient);

        // Return list of appropriate appointments for any chosen patient in the past

        when(appointmentRepository.findByPatientUserIdAndDateLessThan(any(Integer.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/patient/1/past"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForDoctorFuture() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);

        appointment1.setDoctor(doctor);

        // Return list of appropriate appointments for any chosen doctor in the future

        when(appointmentRepository.findByDoctorUserIdAndDateGreaterThanEqual(any(Integer.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/doctor/1/future"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForDoctorPast() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);

        appointment1.setDoctor(doctor);


        // Return list of appropriate appointments for any chosen doctor in the past

        when(appointmentRepository.findByDoctorUserIdAndDateLessThan(any(Integer.class), any(LocalDate.class)))
                .thenReturn(List.of(appointment1));

        // Check the endpoints to make sure they work.
        // Trust the Spring Boot logic does its part on the filtering
        mockMvc.perform(get("/appointments/doctor/1/past"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Create Appointment Success Should Return 201")
    void createAppointment_Success() throws Exception {
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);
        User patient = new User();
        patient.setUserId(2);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);

        // No doctor conflict
        when(appointmentRepository.doctorOverlap(any(),any(),any(),any())).thenReturn(false);
        // No patient conflict
        when(appointmentRepository.patientOverlap(any(),any(),any(),any())).thenReturn(false);
        // Create the appointment
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(newAppointment);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAppointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentId").value(1));

    }

    @Test
    @DisplayName("Create Appointment Fail Doctor Conflict Should Return 409")
    void createAppointment_FailDoctor() throws Exception {
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);
        User patient = new User();
        patient.setUserId(2);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);

        // Doctor conflict
        when(appointmentRepository.doctorOverlap(any(),any(),any(),any())).thenReturn(true);


        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAppointment)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Create Appointment Fail Patient Conflict Should Return 409")
    void createAppointment_FailPatient() throws Exception {
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentId(1);

        User doctor = new User();
        doctor.setUserId(1);
        User patient = new User();
        patient.setUserId(2);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);

        // Patient conflict
        when(appointmentRepository.patientOverlap(any(),any(),any(),any())).thenReturn(true);


        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAppointment)))
                .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("Update Appointment Success Should Return 200")
    void updateAppointment_Success() throws Exception {

        int appointmentId = 1;
        Appointment currentAppointment = new Appointment();
        currentAppointment.setAppointmentId(appointmentId);

        User doctor = new User();
        doctor.setUserId(1);
        User patient = new User();
        patient.setUserId(2);
        currentAppointment.setDoctor(doctor);
        currentAppointment.setPatient(patient);



        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);

        // No doctor conflict
        when(appointmentRepository.doctorOverlapExcluding(any(),any(),any(),any(),any())).thenReturn(false);
        // No patient conflict
        when(appointmentRepository.patientOverlapExcluding(any(),any(),any(),any(),any())).thenReturn(false);
        // Update the appointment
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(currentAppointment);


        Appointment updateRequest = new Appointment();
        updateRequest.setStartTime(LocalTime.of(10, 0));
        updateRequest.setEndTime(LocalTime.of(11, 0));
        updateRequest.setDoctor(doctor);
        updateRequest.setPatient(patient);


        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(currentAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(put("/appointments/" + appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value(1));

    }

    @Test
    @DisplayName("Update Appointment Fail Doctor Conflict Should Return 409")
    void updateAppointment_FailDoctor() throws Exception {
        int appointmentId = 1;
        Appointment currentAppointment = new Appointment();
        currentAppointment.setAppointmentId(appointmentId);

        User doctor = new User();
        doctor.setUserId(1);

        currentAppointment.setDoctor(doctor);


        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);

        // Doctor conflict
        when(appointmentRepository.doctorOverlapExcluding(any(),any(),any(),any(),any())).thenReturn(true);


        mockMvc.perform(put("/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentAppointment)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Update Appointment Fail Patient Conflict Should Return 409")
    void updateAppointment_FailPatient() throws Exception {
        int appointmentId = 1;
        Appointment currentAppointment = new Appointment();
        currentAppointment.setAppointmentId(appointmentId);

        User doctor = new User();
        doctor.setUserId(1);
        User patient = new User();
        patient.setUserId(2);
        currentAppointment.setDoctor(doctor);
        currentAppointment.setPatient(patient);

        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);

        // Patient conflict
        when(appointmentRepository.patientOverlapExcluding(any(),any(),any(),any(),any())).thenReturn(true);


        mockMvc.perform(put("/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentAppointment)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Update Appointment Should Return 404 When Not Found")
    void updateAppointment_NotFound() throws Exception {
        int appointmentId = 999;
        when(appointmentRepository.existsById(appointmentId)).thenReturn(false);

        mockMvc.perform(put("/appointments/" + appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Appointment())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update Appointment Field Only Provided Fields Success Should Return 200")
    void updateAppointmentField_Success() throws Exception {
        int appointmentId = 1;
        Appointment currentAppointment = new Appointment();
        currentAppointment.setAppointmentId(appointmentId);
        currentAppointment.setDate(LocalDate.of(2026, 4, 1));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(currentAppointment));

        Appointment partialUpdate = new Appointment();
        partialUpdate.setDate(LocalDate.of(2026, 4, 22));

        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(patch("/appointments/" + appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-04-22"));
    }

    @Test
    @DisplayName("Update Appointment Field Should Return 404 When Not Found")
    void updateAppointmentField_NotFound() throws Exception {
        int appointmentId = 999;
        when(appointmentRepository.existsById(appointmentId)).thenReturn(false);

        mockMvc.perform(patch("/appointments/" + appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Appointment())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete Appointment With Invalid Id Should Return 404")
    void deleteUserAppointment_DoesNotExist() throws Exception {
        int appointmentId = 999;
        when(appointmentRepository.existsById(appointmentId)).thenReturn(false);

        mockMvc.perform(delete("/appointments/" + appointmentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete Appointment Should Return No Content For That Id")
    void deleteAppointment_NoContent() throws Exception {
        int appointmentId = 1;
        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);

        mockMvc.perform(delete("/appointments/" + appointmentId))
                .andExpect(status().isNoContent());
    }

}