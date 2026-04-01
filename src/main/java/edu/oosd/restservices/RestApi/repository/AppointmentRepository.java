package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

//This extension gives us a lot of FindBy and such for free
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    //For finding appointments for a single day
    List<Appointment> findByDoctorUserIdAndDate(Integer doctorId, LocalDate date);

    //For finding appointment for a range of dates
    List<Appointment> findByDoctorUserIdAndDateBetween(Integer doctorId, LocalDate start, LocalDate end);
}
