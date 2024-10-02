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

    // Adding position field.
    // When you set the position, the salary will be set automatically.
    @Enumerated(EnumType.STRING)
    private Position position;
    private int salary;

    public void setPosition(Position position) {
        this.position = position;
        this.salary = position.getSalary();
    }


    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

}
