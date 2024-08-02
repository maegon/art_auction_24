package com.example.ArtAuction_24.domain.question.controller;



import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String questionCreate(QuestionForm questionForm) {
        return "question/write";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 @RequestParam("questionType") QuestionType questionType) {
        if (bindingResult.hasErrors()) {
            return "question/write";
        }
        this.questionService.create(questionForm, questionType);
        return "redirect:/question/list";
    }

}
