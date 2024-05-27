package com.luisaq.spacetrader.service.auth;

import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    protected User getUserByUsername(String username){
        return this.userRepository.findByUsername(username)
                .orElseThrow();
    }
}
