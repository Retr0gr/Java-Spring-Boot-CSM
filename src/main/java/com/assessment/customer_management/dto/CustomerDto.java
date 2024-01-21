package com.assessment.customer_management.dto;

import com.assessment.customer_management.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private  Long id;
    @NotEmpty(message = "Customer Name is mandatory")
    private String name;

    private Date birthdate;
    private Date last_contact;
    private Date became_customer;
    private Boolean iscustomer;
    @NotEmpty(message = "Employee id is mandatory")
    private User user;
}
