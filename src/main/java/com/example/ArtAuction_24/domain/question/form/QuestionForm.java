package com.example.ArtAuction_24.domain.question.form;

import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200, message = "제목은 200자를 넘을수없습니다.")
    private String subject;

    @Size(max=20000, message = "내용은 20000자를 넘을수없습니다.")
    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    @NotNull(message = "유형을 선택해주세요.")
    private QuestionType questionType;


}
