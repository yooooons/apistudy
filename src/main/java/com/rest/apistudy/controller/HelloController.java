package com.rest.apistudy.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("/helloworld/string")
    @ResponseBody
    public String helloWorldString() {
        return "hello World";
    }

    @GetMapping("/helloworld/json")
    @ResponseBody
    //대상 클래스 필드가 public 이거나 getter가 있어야한다
    public Hello helloWorldJson() {
        Hello hello = new Hello();
        hello.setMessage("hello world");
        return hello;
    }



    @Getter
    @Setter
    public static class Hello {
        private String message;
    }





}
