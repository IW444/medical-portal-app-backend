package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
