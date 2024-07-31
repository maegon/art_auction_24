package com.example.ArtAuction_24.domain.question.controller;


import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    @GetMapping("/list")
    public String questionList(Model model){
        List<Question> questionList = questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "question/list";

    }



    @GetMapping(value = "/detail/{id}")



    public String detail(Model model, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/detail";
    }


    @GetMapping("/create")
    public String questionCreate() {
        return "question/write";
    }

    @PostMapping("/create")
    public String questionCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
        this.questionService.create(subject, content);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }

}
