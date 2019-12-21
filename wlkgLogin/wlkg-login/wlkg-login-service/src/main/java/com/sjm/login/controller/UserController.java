package com.sjm.login.controller;

import com.sjm.login.pojo.User;
import com.sjm.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        System.out.println(data);
        System.out.println(type);
        Boolean b = this.userService.checkUserData(data, type);
        if (b == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(b);
    }

    @GetMapping("/query/{type}")
    public ResponseEntity<User> loginUser(User user ,@PathVariable(value = "type") Integer type) {
        System.out.println(user);
        System.out.println(type);
        User user1 = userService.loginUser(user,type);
        return ResponseEntity.ok(user1);
    }
}
