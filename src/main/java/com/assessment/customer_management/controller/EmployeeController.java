package com.assessment.customer_management.controller;

import com.assessment.customer_management.dto.CustomerDto;
import com.assessment.customer_management.model.Customer;
import com.assessment.customer_management.model.User;
import com.assessment.customer_management.repository.CustomerRepository;
import com.assessment.customer_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

//Employee Controller
@Controller
public class EmployeeController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/employee")
    public String employee_home(){
        return "home";
    }

    //Show customers based on keyword, and if they are customers or not
    @GetMapping({"/employee/customers", "/search"})
    public String customers(Principal principal, Model model, String keyword, String isCustomer) {
        User loggedUser = userRepository.findByUsername(principal.getName());
        if(keyword != null){
            if(isCustomer != null){
                List<Customer> customers = customerRepository.findByKeywordAndIscustomerAndUser(keyword, loggedUser.getId());
                model.addAttribute("customers", customers);
            }else {
                List<Customer> customers = customerRepository.findByKeywordAndUser(keyword, loggedUser.getId());
                model.addAttribute("customers", customers);
            }
        }else {
            List<Customer> customers = customerRepository.findAllByUser(Optional.of(loggedUser));
            model.addAttribute("customers", customers);
        }

        return  "customers";
    }

    //Delete customer
    @PostMapping(value = "employee/customers", params = "action=delete")
    public String deleteCustomer(@RequestParam(value = "customerId") List<Long> ids) {
        for (Long id : ids) {
            customerRepository.deleteById(id);
        }
        return "customers";
    }

    //Edit customer info
    @PostMapping(value = "employee/customers", params = "action=edit")
    public String editCustomer(@RequestParam(value = "customerId") Long id, Model model){
        Optional<Customer> customer = customerRepository.findById(id);
        model.addAttribute("customer", customer);
        return "edit_customer";
    }

    //Save new customer info on db
    @Transactional
    @PostMapping(value = "employee/edit_customer")
    public String finalise_edit(@ModelAttribute CustomerDto customerDto, Long id){
        customerRepository.setUserInfoById(customerDto.getName(), customerDto.getBirthdate(),
                customerDto.getLast_contact(), customerDto.getBecame_customer(), customerDto.getIscustomer(),
                customerDto.getUser(), customerDto.getId());

        return "customers";
    }

    //Find only non customers
    @GetMapping("/employee/contacts")
    public String contacts(Model model) {
        List<Customer> contacts = customerRepository.findAllByIscustomer(false);
        model.addAttribute("contacts", contacts);
        return "contacts";
    }


    //Add new customer
    @GetMapping("/employee/add_customer")
    public String add_customer(Model model){
        CustomerDto customerDto = new CustomerDto();
        model.addAttribute("customerDto", customerDto);
        return "add_customer";
    }

    //Save new customer on db
    @PostMapping("/employee/add_customer")
    public String add_customer_save(@ModelAttribute CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setBirthdate(customerDto.getBirthdate());
        customer.setLast_contact(customerDto.getLast_contact());
        customer.setBecame_customer(customerDto.getBecame_customer());
        customer.setIscustomer(customerDto.getIscustomer());
        customer.setUser(customerDto.getUser());
        customerRepository.save(customer);
        return "add_customer";
    }


    @GetMapping("/employee/{eid}")
    public String employee(@PathVariable long eid, Model model) {
        Optional<User> employee = userRepository.findById(eid);
        if(employee.isPresent()){
            model.addAttribute("employee", employee.get());
            return "employee";
        }else{
            return "error";
        }
    }
}
