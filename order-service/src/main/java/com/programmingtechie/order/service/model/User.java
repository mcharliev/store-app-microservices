package com.programmingtechie.order.service.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@ToString
@Table(name = "user_info")
public class User {
    @Id
    @SequenceGenerator(name = "user_infoSequence", sequenceName = "user_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_infoSequence")
    @Column(name = "id")
    private Long id;

//    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

//    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @Column(name = "role")
    private String role;

}
