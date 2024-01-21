package com.assessment.customer_management.controller;

import com.assessment.customer_management.dto.CustomerDto;
import com.assessment.customer_management.model.Customer;
import com.assessment.customer_management.model.User;
import com.assessment.customer_management.repository.CustomerRepository;
import com.assessment.customer_management.repository.UserRepository;
import jdk.jfr.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Manager Controller
@Controller
public class ManagerController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("/manager")
    public String manager_dashboard() {return "manager/manager_home";}

    //Show all employees
    @GetMapping("manager/employees")
    public String managerEmployees(Model model){
        List<User> employees = userRepository.findAllByRole("ROLE_EMPLOYEE");
        model.addAttribute("employees", employees);
        return "manager/employees";
    }

    //Show customers of employee
    @PostMapping(value = "manager/emp-customers", params = "action=viewCustomers")
    public String employeesCustomers(@RequestParam(value = "employeeId") Long id, Model model){
        Optional<User> user = userRepository.findById(id);
        String employeeName = user.get().getUsername();
        List<Customer> customers = customerRepository.findAllByUser(user);

        model.addAttribute("customers", customers);
        model.addAttribute("employeeName", employeeName);
        return "manager/emp-customers";
    }


    //Stats----------------------------------------------------------------
    @GetMapping("/manager/stats")
    public String stats(Model model){
        //Tolal Customer to Non-Customer percentages
        List<Customer> customers = customerRepository.findAllByIscustomer(true);
        List<Customer> allCustomers = customerRepository.findAll();
        float percentageOfCustomers = (float) customers.size() / allCustomers.size() * 100;
        float percentageOfContacts =  100 - percentageOfCustomers;

        //Employee Customer to Non-Customer percentages
        List<User> employees = userRepository.findAllByRole("ROLE_EMPLOYEE");
        List<List<String>> employeesCustomers = new ArrayList<>();
        for (User employee : employees){
            long customerCount = customerRepository.countByUserAndIscustomer(employee, true);
            long allcustomerCount = customerRepository.countByUser(employee);
            List<String> innerList = new ArrayList<String>();
            innerList.add(employee.getUsername());
            innerList.add(String.valueOf(allcustomerCount));
            innerList.add(String.valueOf(customerCount));
            employeesCustomers.add(innerList);
        }
        //System.out.println("List of lists:" + employeesCustomers);
        model.addAttribute("percentageOfTotalCustomers", percentageOfCustomers);
        model.addAttribute("percentageOfTotalContacts", percentageOfContacts);
        model.addAttribute("ListOfEmployees", employeesCustomers);
        model.addAttribute("totalContacts", allCustomers.size());
        return "manager/stats";
    }
}


