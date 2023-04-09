package com.honglog.api.controller;

import com.honglog.api.config.data.UserSession;
import com.honglog.api.exception.InvalidRequest;
import com.honglog.api.request.PostCreate;
import com.honglog.api.request.PostEdit;
import com.honglog.api.request.PostSearch;
import com.honglog.api.response.PostResponse;
import com.honglog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(">>>{}", userSession.getId());
        return userSession.getId();
    }

    @GetMapping("/bar")
    public String bar(UserSession userSession) {

        return "인증 노필요";
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {

        return postService.getList(postSearch);
    }

    // 글 등록
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {

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
