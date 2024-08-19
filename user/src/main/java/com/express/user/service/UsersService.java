package com.express.user.service;

import com.express.user.dto.UserRequest;
import com.express.user.dto.UserResponse;
import com.express.user.dtoMaper.UserDtoMapper;
import com.express.user.entity.Role;
import com.express.user.entity.User;
import com.express.user.exception.DataAccessException;
import com.express.user.exception.DuplicationException;
import com.express.user.exception.ValidationException;
import com.express.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User Register(UserRequest userRequest) {
        log.info("UsersService::Register()::Creating user started");

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.error("UsersService::Register()::Email already exists: {} ", userRequest.getEmail());
            throw new DuplicationException("Email already exists:"+ userRequest.getEmail());
        }
        if (userRepository.existsByContact(userRequest.getContact())) {
            log.error("UsersService::Register()::Contact already exists {} ", userRequest.getContact());
            throw new DuplicationException("Contact already exists "+ userRequest.getContact());
        }
        User user = UserDtoMapper.convertToUser(userRequest, passwordEncoder);
        user = userRepository.save(user);
        log.info("UsersService::Register()::Creating user completed");
        return user;
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        log.info("UsersService::createUser()::Creating user started");

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.error("UsersService::createUser()::Email already exists: {} ", userRequest.getEmail());
            throw new DuplicationException("Email already exists:"+ userRequest.getEmail());
        }
        if (userRepository.existsByContact(userRequest.getContact())) {
            log.error("UsersService::createUser()::Contact already exists {} ", userRequest.getContact());
            throw new DuplicationException("Contact already exists "+ userRequest.getContact());
        }

        if (userRequest.getContact().length() != 10) {
            log.error("UsersService::createUser()::Contact number should be of 10 digits {} ", userRequest.getContact());
            throw new ValidationException("Contact number should be of 10 digits "+ userRequest.getContact());
        }
        User user = UserDtoMapper.convertToUser(userRequest, passwordEncoder);
        user = userRepository.save(user);
        log.info("UsersService::createUser()::Creating user completed");
        return UserDtoMapper.convertToResponse(user);
    }

    public UserResponse getUserById(Long id) {
        log.info("UsersService::getUserById()::Fetching user by id started");
        User user = userRepository.findById(id).orElseThrow(() -> new DataAccessException("User not found with id: " + id));
        log.info("UsersService::getUserById()::Fetching user by id completed");
        return UserDtoMapper.convertToResponse(user);
    }

    public User getUserEntityById(Long id) {
        log.info("UsersService::getUserEntityById()::Fetching user by id started");
        User user = userRepository.findById(id).orElseThrow(() -> new DataAccessException("User not found with id: " + id));
        log.info("UsersService::getUserEntityById()::Fetching user by id completed");
        return user;
    }

    public UserResponse getUserByEmail(String email) {
        log.info("UsersService::getUserByEmail()::Fetching user by email started");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataAccessException("User not found with email: " + email));
        log.info("UsersService::getUserByEmail()::Fetching user by email completed");
        return UserDtoMapper.convertToResponse(user);
    }

    public UserResponse getUserByContact(String contact) {
        log.info("UsersService::getUserByContact()::Fetching user by contact started");
        User user = userRepository.findByContact(contact).orElseThrow(() -> new DataAccessException("User not found with contact: " + contact));
        log.info("UsersService::getUserByContact()::Fetching user by contact completed");
        return UserDtoMapper.convertToResponse(user);
    }


    public List<UserResponse> getAllUsers(String category) {
        log.info("UsersService::getAllUsers()::Fetching all users started with category: {} ", category);
        List<User> users = userRepository.findAllByRole(Role.valueOf(category.toUpperCase())).orElseThrow(() -> new DataAccessException("No users found with category: " + category));
        log.info("UsersService::getAllUsers()::Fetching all users completed with category: {} ", category);
        return users.stream().map(UserDtoMapper::convertToResponse).collect(Collectors.toList());
    }

    public boolean existById(Long id) {
        log.info("UsersService::existById()::Checking user existence by id started");
        return userRepository.existsById(id);

    }
    

}
