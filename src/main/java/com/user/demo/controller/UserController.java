package com.user.demo.controller;

import com.user.demo.dto.UserDto;
import com.user.demo.dto.UserLoginResponseDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.service.UserService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto) {
        try {
            UserResponseDTO newUser = userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (CustomException e) {
            return handleCustomException(e);
        }
    }
    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestHeader("Authorization") String token) {
        try {
            userService.validateToken(token);
            UserLoginResponseDTO userResponse = userService.getUserByToken(token);
            return ResponseEntity.ok(userResponse);
        } catch (CustomException e) {
            return handleCustomException(e);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }
}



