package com.facial.entity;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_shifts")
@Entity
public class UserShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "shift_id")
    Shift shift;

    @OneToOne
    @JoinColumn(name = "attendance_id")
    Attendance attendance;

    Date shiftDate;

    Timestamp createdAt;
    Timestamp updatedAt;
}
