package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//This extension gives us a lot of FindBy and such for free
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    //For finding appointments for a doctor for a single day
    List<Appointment> findByDoctorUserIdAndDate(Integer doctorId, LocalDate date);

    //For finding appointment for a doctor for a range of dates
    List<Appointment> findByDoctorUserIdAndDateBetween(Integer doctorId, LocalDate start, LocalDate end);

    //For finding future appointments for a doctor
    List<Appointment> findByDoctorUserIdAndDateGreaterThanEqual(Integer doctorId, LocalDate date);

    //For finding past appointments for a doctor
    List<Appointment> findByDoctorUserIdAndDateLessThan(Integer doctorId, LocalDate date);

    //For finding future appointments for a patient
    List<Appointment> findByPatientUserIdAndDateGreaterThanEqual(Integer patientId, LocalDate date);

    //For finding past appointments for a patient
    List<Appointment> findByPatientUserIdAndDateLessThan(Integer patientId, LocalDate date);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.userId = :doctorId " +
            "AND a.date = :date " +
            "AND a.startTime < :endTime AND a.endTime > :startTime")
    boolean doctorOverlap(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patient.userId = :patientId " +
            "AND a.date = :date " +
            "AND a.startTime < :endTime AND a.endTime > :startTime")
    boolean patientOverlap(
            @Param("patientId") Integer patientId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.userId = :doctorId " +
            "AND a.date = :date " +
            "AND a.startTime < :endTime AND a.endTime > :startTime " +
            "AND a.appointmentId != :excludeId")
    boolean doctorOverlapExcluding(
            @Param("doctorId") Integer doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Integer excludeId
    );

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patient.userId = :patientId " +
            "AND a.date = :date " +
            "AND a.startTime < :endTime AND a.endTime > :startTime " +
            "AND a.appointmentId != :excludeId")
    boolean patientOverlapExcluding(
            @Param("patientId") Integer patientId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Integer excludeId
    );

}
