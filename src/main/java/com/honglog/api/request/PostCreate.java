package com.honglog.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;
}
