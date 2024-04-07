package me.projects.bootblog.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller //컨트롤러 라는 것을 명시
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){ //뷰로 데이터를 넘겨주는 모델 객체
        Person examplePerson = new Person();

        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson); //사람정보 저장
        model.addAttribute("today", LocalDate.now()); //날자 저장

        return "example"; // @Controller라는 애너테이션을 보고 example.html 라는 뷰 조회
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}