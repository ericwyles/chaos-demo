package com.ericwyles.chaosdemo.remote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("remote")
@Slf4j
public class RemoteController {

    @GetMapping("/random")
    public Integer random() {
        Random generator = new Random();
        return generator.nextInt(1000);
    }
}
