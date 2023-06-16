package com.springbootdeveloper.blog.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("thymeleaf")
public class ExampleController {

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }

    @GetMapping("/example")
    public String thymeleafExample(Model model){
        Person person = new Person();
        person.setId(1L);
        person.setName("박동건");
        person.setAge(26);
        person.setHobbies(List.of("영화보기", "롤토체스"));

        // model에 객체 정보 저장
        model.addAttribute("person", person);
        model.addAttribute("today", LocalDate.now());
        
        // 해당 view로 이동
        return "example";
    }
}
