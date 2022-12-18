package com.honglog.api.controller;

import com.honglog.api.request.PostCreate;
import com.honglog.api.response.PostResponse;
import com.honglog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/posts")

    public String get() {
        return "Hello World";
    }


    // 글 등록
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        //저장한 데이터 리스폰스로 응답
        postService.write(request);
    }

    @PostMapping("/posts")

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        //저장한 데이터 리스폰스로 응답
        return postService.get(postId);
    }
}
