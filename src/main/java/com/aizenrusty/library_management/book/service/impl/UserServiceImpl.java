package com.aizenrusty.library_management.book.service.impl;

import com.aizenrusty.library_management.book.dto.UserInputDto;
import com.aizenrusty.library_management.book.dto.UserOutputDto;
import com.aizenrusty.library_management.book.entity.Users;
import com.aizenrusty.library_management.book.repository.UserRepository;
import com.aizenrusty.library_management.book.service.UserService;
import com.aizenrusty.library_management.exception.GlobalExceptionHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createUser(UserInputDto userInputDto) {

        if(userRepository.existsByEmail(userInputDto.getEmail())) {
            throw new GlobalExceptionHandler.DuplicateResourceException("Email already exists");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users user = new Users();
        user.setName(userInputDto.getName());
        user.setEmail(userInputDto.getEmail());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setPhoneNumber(userInputDto.getPhoneNumber());
        user.setAddress(userInputDto.getAddress());
        user.setRole(Users.Role.USER);
        userRepository.save(user);
    }

    @Override
    public List<UserOutputDto> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserOutputDto userOutputDto = new UserOutputDto();
            userOutputDto.setId(user.getId());
            userOutputDto.setName(user.getName());
            userOutputDto.setEmail(user.getEmail());
            userOutputDto.setPhoneNumber(user.getPhoneNumber());
            userOutputDto.setAddress(user.getAddress());
            userOutputDto.setRole(user.getRole().name());
            return userOutputDto;
        }).toList();
    }

    @Override
    public UserOutputDto getUserById(Long id) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User not found");
        }
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(user.getId());
        userOutputDto.setName(user.getName());
        userOutputDto.setEmail(user.getEmail());
        userOutputDto.setPhoneNumber(user.getPhoneNumber());
        userOutputDto.setAddress(user.getAddress());
        return userOutputDto;
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserInputDto userInputDto) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User not found");
        }
        user.setName(userInputDto.getName());
        user.setEmail(userInputDto.getEmail());
        user.setPhoneNumber(userInputDto.getPhoneNumber());
        user.setAddress(userInputDto.getAddress());
        userRepository.save(user);
    }
}
