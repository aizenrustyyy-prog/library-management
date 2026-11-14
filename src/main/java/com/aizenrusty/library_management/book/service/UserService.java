package com.aizenrusty.library_management.book.service;

import com.aizenrusty.library_management.book.dto.UserInputDto;
import com.aizenrusty.library_management.book.dto.UserOutputDto;

import java.util.List;

public interface UserService {
    void createUser(UserInputDto userInputDto);

    List<UserOutputDto> getAllUsers();

    UserOutputDto getUserById(Long id);

    void updateUser(Long id, UserInputDto userInputDto);
}
