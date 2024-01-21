package com.assessment.customer_management.controller;

import com.assessment.customer_management.dto.CustomerDto;
import com.assessment.customer_management.dto.UserDto;
import com.assessment.customer_management.model.Customer;
import com.assessment.customer_management.model.User;
import com.assessment.customer_management.repository.CustomerRepository;
import com.assessment.customer_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Admin controller
@Controller
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    private String admin_dashboard() {return "admin/admin_home";}

    //Show customers, based on keyword, and if they are customers
    @GetMapping(path ={"/admin/customers", "/admin/search"})
    public String customers(Model model, String keyword, String isCustomer) {
        if(keyword != null){
            if(isCustomer != null){
                List<Customer> customers = customerRepository.findByKeywordAndIscustomer(keyword);
                model.addAttribute("customers", customers);
            }else {
            List<Customer> customers = customerRepository.findByKeyword(keyword);
            model.addAttribute("customers", customers);
            }
        }else {
            List<Customer> customers = customerRepository.findAll();
            model.addAttribute("customers", customers);
        }

        return  "admin/customers";
    }

    //Find users based on keyword and role
    @GetMapping(path = {"admin/users", "admin/search_user"})
    public String users(Model model, String keyword, String role) {
        if(keyword != null || role != null){
           List<User> users = userRepository.findByKeywordAndRole(keyword, role);
           model.addAttribute("users", users);
        }else {
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
        }
        return "admin/users";
    }


    //Delete user function
    @PostMapping(value = "admin/users", params = "action=delete")
    public  String deleteUser(@RequestParam(value = "userId") List<Long> ids){
        for (Long id : ids) {
            userRepository.deleteById(id);
        }
        return "admin/users";
    }

    //Edit user info function
    @PostMapping(value = "admin/users", params = "action=edit")
    public String editUser(@RequestParam(value = "userId") Long id, Model model){
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user);
        return "admin/edit_user";
    }

    //Save new user info on db
    @Transactional
    @PostMapping(value = "admin/edit_user")
    public String finalise_edit_user(@ModelAttribute UserDto userDto, Long id){
        userRepository.setUserInfoById(userDto.getUsername(), userDto.getRole(), userDto.getId());
        return "admin/users";
    }

    //Add new user function
    @GetMapping(value = "admin/add_user")
    public String addUser(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "admin/add_user";
    }

    //Save new user
    @PostMapping(value = "admin/add_user")
    public String addUser_save(@ModelAttribute UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return "admin/users";
    }

    //Delete customer from db
    @PostMapping(value = "admin/customers", params = "action=delete")
    public String delete(@RequestParam(value = "customerId") List<Long> ids) {
        for (Long id : ids) {
            customerRepository.deleteById(id);
        }
        return "admin/customers";
    }

    //Edit customer info
    @PostMapping(value = "admin/customers", params = "action=edit")
    public String edit(@RequestParam(value = "customerId") Long id, Model model) {
        Optional<Customer> customer = customerRepository.findById(id);
        model.addAttribute("customer", customer);
        return "admin/edit_customer";
    }

    //Save new customer info on db
    @Transactional
    @PostMapping(value = "admin/edit_customer")
    public String finalise_edit(@ModelAttribute CustomerDto customerDto, Long id){
        customerRepository.setUserInfoById(customerDto.getName(), customerDto.getBirthdate(),
                customerDto.getLast_contact(), customerDto.getBecame_customer(), customerDto.getIscustomer(),
                customerDto.getUser(), customerDto.getId());

        return "admin/customers";
    }
    

}
