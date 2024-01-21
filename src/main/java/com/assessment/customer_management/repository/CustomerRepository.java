package com.assessment.customer_management.repository;

import com.assessment.customer_management.model.Customer;
import com.assessment.customer_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer>  findAllByIscustomer(Boolean iscustomer);
    List<Customer>  findAllByUser(Optional<User> user);

    long countByUserAndIscustomer(User user, Boolean iscustomer);

    long countByUser(User user);

    @Query(value = "select * from customers c where c.name like %:keyword%", nativeQuery = true)
    List<Customer> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from customers c where c.name like %:keyword% and c.iscustomer=1", nativeQuery = true)
    List<Customer> findByKeywordAndIscustomer(@Param("keyword") String keyword);

    @Modifying
    @Query("update Customer c set c.name = ?1, c.birthdate = ?2, c.last_contact = ?3, c.became_customer = ?4, c.iscustomer = ?5, c.user = ?6 where c.id = ?7")
    void setUserInfoById(String name, Date birthdate, Date last_contact, Date became_customer, Boolean iscustomer, User user, Long userId);

    @Query(value = "select * from customers c where c.name like %:keyword% and c.iscustomer=1 and c.user_id = :loggeduser", nativeQuery = true)
    List<Customer> findByKeywordAndIscustomerAndUser(@Param("keyword") String keyword,@Param("loggeduser") Long loggeduser);

    @Query(value = "select * from customers c where c.name like %:keyword% and c.user_id= :loggeduser", nativeQuery = true)
    List<Customer> findByKeywordAndUser(@Param("keyword") String keyword,@Param("loggeduser") Long loggeduser);

}
