package com.example.demo.controller;

import com.example.demo.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bucket")
public class BucketController {

    @Autowired
    BucketService bucketService;
    @GetMapping("/helloWorld")
    public String helloWorld(){
        if (bucketService.acquire() == 0) {
            return "系统忙，请稍后重试";
        }
        return "hello";
    }
}
