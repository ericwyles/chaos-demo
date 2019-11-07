package com.ericwyles.chaosdemo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value= "remoteRandom", url="http://localhost:8080")
public interface RemoteClient {
    @GetMapping("/remote/random")
    Integer getRandom();
}
