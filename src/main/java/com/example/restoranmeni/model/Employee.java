package com.example.restoranmeni.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Data
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private Integer salary;
    private String position;

    @Column(insertable = false, updatable = false)
    private String username; // add this field

    @Enumerated(EnumType.STRING) // add this annotation
    private Role role; // add this field

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

}
