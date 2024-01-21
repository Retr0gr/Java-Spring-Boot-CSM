package com.assessment.customer_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(length = 250, nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(20) default 'ROLE_EMPLOYEE'")
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Customer> customers;

}
