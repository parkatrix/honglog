package com.honglog.api.service;

import com.honglog.api.domain.Post;
import com.honglog.api.repository.PostRepository;
import com.honglog.api.request.PostCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {


    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.",post.getContent());

    }

    @Test
    @DisplayName("글이 없다면 에러")
    void test2() {
        //given
        Long postId = 1L;
        //then
        assertThrows(IllegalArgumentException.class, () -> postService.get(postId));

    }

    @Test
    @Transactional
    @DisplayName("글 1개 조회")
    void test3() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        Post savedPost = postRepository.save(post);

        Post findPost = postService.get(savedPost.getId());

        assertTrue(savedPost.equals(findPost));
    }

}