package com.example.ArtAuction_24.question.controller;


import com.example.ArtAuction_24.question.entity.Question;
import com.example.ArtAuction_24.question.repository.QuestionRepository;
import com.example.ArtAuction_24.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @GetMapping("/question")
    public String questionList(Model model){
        List<Question> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "question/list";

    }
}
