package com.assessment.customer_management.repository;



import com.assessment.customer_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    List<User> findAllByRole(String role);
    User findByUsername(String username);

    @Query(value = "select * from users u where u.username like %:keyword%", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from users u where u.username like %:keyword% and u.role = :roles", nativeQuery = true)
    List<User> findByKeywordAndRole(@Param("keyword") String keyword, @Param("roles") String roles);

    @Modifying
    @Query("update User u set u.username = ?1, u.role = ?2 where u.id = ?3")
    void setUserInfoById(String username, String role, Long id);
}
