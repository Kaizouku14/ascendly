package com.example.backend.auth;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    public boolean Login (String email, String password) {
        return false;
    }

    public boolean Register (String username, String email, String password) {
        return false;
    }

}
