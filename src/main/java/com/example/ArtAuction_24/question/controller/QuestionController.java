package com.example.ArtAuction_24.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    @GetMapping("/question")
    public String questionList(){
        return "myPage/myPage";
    }
}
