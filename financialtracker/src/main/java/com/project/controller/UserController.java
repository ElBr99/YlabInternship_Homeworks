package com.project.controller;

import com.project.dtos.ChangeInfoDto;
import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable("email") String email) {
        userService.deleteAccount(email);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('USER')")
    public void blockCurrentUser() {
        userService.blockUser();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    public void updateUser(@RequestBody ChangeInfoDto changeInfoDto) {
        userService.changeInfo(changeInfoDto);
    }
}
