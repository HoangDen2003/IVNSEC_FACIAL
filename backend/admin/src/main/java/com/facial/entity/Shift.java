package com.facial.entity;

import java.sql.Time;
import java.sql.Timestamp;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    Time startTime;
    Time endTime;

    @OneToOne(mappedBy = "shift")
    UserShift userShift;

    Timestamp createdAt;
    Timestamp updatedAt;
}
