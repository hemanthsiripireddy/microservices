package com.siripireddy.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {
    @GetMapping("/hello")
    public String helloWorld(){
        return "Hello1 World from Accounts Application";
    }
}
