package com.honglog.api.controller;

import com.honglog.api.exception.InvalidRequest;
import com.honglog.api.request.PostCreate;
import com.honglog.api.request.PostEdit;
import com.honglog.api.request.PostSearch;
import com.honglog.api.response.PostResponse;
import com.honglog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    // 글 등록
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        request.validate();

        //저장한 데이터 리스폰스로 응답
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {

        //저장한 데이터 리스폰스로 응답
        return postService.get(postId);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
