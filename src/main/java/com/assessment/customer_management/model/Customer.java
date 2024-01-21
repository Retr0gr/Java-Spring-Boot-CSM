package com.assessment.customer_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = true)
    private Date birthdate;
    @Column
    private Date last_contact;
    @Column
    private Date became_customer;
    @Column(columnDefinition = "boolean default FALSE")
    private Boolean iscustomer;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
