package com.brunocosta.taskflow;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/health")
public class HealthController {
    @Get
    public String ok(){
        return "Ok";
    }
}
