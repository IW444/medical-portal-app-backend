CREATE DATABASE IF NOT EXISTS medical_portal;

--Note that we want at least 60 characters for the hashed password.
--Also password and role are reserved words, so we need backticks.
CREATE TABLE medical_portal.users(
    userId INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    username VARCHAR(100) UNIQUE,
    `password` VARCHAR(60),
    `role` VARCHAR(50),
    lastLogin DATETIME,
    lastPasswordChange DATETIME
);

--Similarly date and timestamp are reserved and need backticks
CREATE TABLE medical_portal.appointments(
    appointmentId INT PRIMARY KEY AUTO_INCREMENT,
    `date` DATE,
    startTime TIME,
    endTime TIME,
    patientId INT,
    doctorId INT,
    `timestamp` DATETIME,
    FOREIGN KEY (patientId) REFERENCES users(userId),
    FOREIGN KEY (doctorId) REFERENCES users(userId)
);