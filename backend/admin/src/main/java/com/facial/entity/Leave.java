package com.facial.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

import com.facial.constant.StatusLeave;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String description;

    Timestamp startTime;
    Timestamp endTime;

    @Enumerated(EnumType.STRING)
    StatusLeave status;

    Timestamp createdAt;
    Timestamp updatedAt;
}
