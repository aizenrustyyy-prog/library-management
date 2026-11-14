package com.aizenrusty.library_management.book.controller;

import com.aizenrusty.library_management.book.dto.UserInputDto;
import com.aizenrusty.library_management.book.dto.UserOutputDto;
import com.aizenrusty.library_management.book.service.UserService;
import com.aizenrusty.library_management.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        userService.createUser(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserOutputDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<UserOutputDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable Long id,
                                                          @Valid @RequestBody UserInputDto userInputDto) {
        userService.updateUser(id, userInputDto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully"));
    }

}
