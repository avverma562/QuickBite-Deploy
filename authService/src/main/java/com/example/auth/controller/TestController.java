package com.example.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Protected API working!";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Admin access granted!";
    }

    @GetMapping("/user/test")
    public String userTest() {
        return "User access granted!";
    }
}