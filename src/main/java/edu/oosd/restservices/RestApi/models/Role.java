package edu.oosd.restservices.RestApi.models;

/**
 * Enum representing the roles a user can have within the medical portal.
 */
public enum Role {
    /**
     * System admin with full access to user
     * and appointment management.
     */
    ADMIN,
    /**
     * Doctor with access to the Doctor dashboard
     * and past and future appointment.
     */
    DOCTOR,
    /**
     * Patient with access to the Patient dashboard
     * and their own past and future appointments.
     */
    PATIENT
}
