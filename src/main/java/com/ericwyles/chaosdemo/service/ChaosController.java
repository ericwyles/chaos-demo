package com.ericwyles.chaosdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service")
@Slf4j
public class ChaosController {

    ChaosService chaosService;

    @Autowired
    public ChaosController(ChaosService chaosService) {
        this.chaosService = chaosService;
    }

    @GetMapping("/hello-resilient")
    public String hello() {
        return String.format("Hello, your message id is %s", chaosService.getMessageId());
    }

    @GetMapping("/hello-brittle")
    public String helloBrittle() {
        return String.format("Hello, your message id is %s", chaosService.getMessageIdBrittle());
    }
}
