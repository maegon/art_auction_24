package com.example.ArtAuction_24.domain.question.controller;



import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.answer.service.AnswerService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.post.entity.Post;
import com.example.ArtAuction_24.domain.post.service.PostService;
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
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;
    private final AnswerService answerService;
    private final PostService postService;

    @GetMapping("/list")
    public String questionList(Model model){
        List<Post> postList = postService.findAll();
        model.addAttribute("postList", postList);

        return "question/list";

    }



    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/detail";
    }


    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm,Model model) {
        List<Question> questionList= questionService.findAll();
        model.addAttribute("questionList", questionList);
        return "question/write";
    }

    @PostMapping("/create")
    public String questionCreateAddImg(@Valid QuestionForm questionForm,
                                       BindingResult bindingResult,
                                       @RequestParam("thumbnail") MultipartFile thumbnail,
                                       Principal principal) {
        Member member = memberService.getMember(principal.getName());
        if (bindingResult.hasErrors()) {
            return "question/write";
        }
        if (thumbnail != null && !thumbnail.isEmpty()) {
            // Handle the file upload logic here
            this.questionService.create(questionForm, thumbnail, member);
        } else {
            // Handle the logic without the file
            this.questionService.create(questionForm, member);
        }


        return "redirect:/question/list";
    }


}
