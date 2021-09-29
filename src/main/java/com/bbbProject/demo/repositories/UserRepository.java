package com.bbbProject.demo.repositories;



import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bbbProject.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAll();
	
	User findByEmail(String email);
}
