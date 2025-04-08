package com.facial.entity;

import java.sql.Time;
import java.sql.Timestamp;

import jakarta.persistence.*;

import com.facial.constant.StatusAttendance;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "attendances")
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(mappedBy = "attendance")
    UserShift userShift;

    Time checkInTime;
    Time checkOutTime;

    Integer workedHours;

    @Enumerated(EnumType.STRING)
    StatusAttendance status;

    Timestamp createdAt;
    Timestamp updatedAt;
}
