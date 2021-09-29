package com.bbbProject.demo.services;

import org.springframework.stereotype.Service;

import com.bbbProject.demo.models.User;
import com.bbbProject.demo.repositories.UserRepository;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserService {
	
	private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findUserByEmail(String email) {
    	return this.userRepository.findByEmail(email);
    }
    
    public User findById(Long id) {
		Optional<User> optional = this.userRepository.findById(id);
		if( optional.isPresent() ) {
			return optional.get();
		} else {
			return null;
		}
	}
    
    public User login(User user) {
    	
    	User foundUser = this.findUserByEmail(user.getEmail());
    	
    	if ( foundUser == null || !BCrypt.checkpw(user.getPassword(), foundUser.getPassword()) ) return null; //checkpw(incomingPW, existing users PW)
    	
    	return foundUser;
    }
    
    public User register(User user) {
    	
    	if ( this.findUserByEmail(user.getEmail()) == null ) {
    		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    		user.setPassword(hashed);
    		
    		user = this.userRepository.save(user);
    		
//    		// not required unless in specs
//    		if( user.getId() == 1 ) {
//    			user.setIsAdmin(true);
//    			this.userRepository.save(user);
//    		}
    		
    		return user;
    	}
    	
    	return null;
    }
}
