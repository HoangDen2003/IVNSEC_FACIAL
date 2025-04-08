package com.facial.entity;

import java.sql.*;
import java.util.List;

import jakarta.persistence.*;

import com.facial.constant.StatusUser;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String email;
    String password;

    String role;

    @OneToMany(mappedBy = "user")
    List<Leave> leave;

    // mapped dung o entity khong chua khoa ngoai
    // JoinColumn dung o entity chua khoa ngoai
    @OneToOne(mappedBy = "user")
    UserShift userShift;

    @Enumerated(EnumType.STRING)
    StatusUser status;

    Timestamp created_at;
    Timestamp updated_at;
}
