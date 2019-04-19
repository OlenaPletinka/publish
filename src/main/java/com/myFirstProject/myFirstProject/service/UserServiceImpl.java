package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.converter.ReqConverterServiceImpl;
import com.myFirstProject.myFirstProject.dto.UserReq;
import com.myFirstProject.myFirstProject.exception.UserNotValidForSaveException;
import com.myFirstProject.myFirstProject.model.User;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ReqConverterServiceImpl reqConverterService;

    @Override
    public Optional<User> getUserByLoginAndPassword(String login, String password) {

        return userRepository.getUserByLoginAndPassword(login, password);
    }

    @Override
    public Long saveUser(UserReq userReq)  {
        logger.info("Get user to update {}", userReq);
        User user = reqConverterService.convert(userReq);
        if(!userRepository.existsByLogin(user.getLogin())) {
            userRepository.save(user);
            Long id = user.getId();
            logger.info("User saved with id {}", id);

            return id;
        }else {
            logger.info("User this such login {} is already exist", user.getLogin());
            throw new UserNotValidForSaveException("User is not valid for save");
        }
    }
}
