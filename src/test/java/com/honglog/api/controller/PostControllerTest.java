package com.honglog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honglog.api.domain.Post;
import com.honglog.api.repository.PostRepository;
import com.honglog.api.request.PostCreate;
import com.honglog.api.request.PostEdit;
import com.honglog.api.request.PostSearch;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청 시 post를 조회한다")
    void postTest() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(postCreate);

        System.out.println("json = " + json);

        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(("글 작성 요청 시 title 값은 필수여!!"))
    void postTest2() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(postCreate);


        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName(("글 작성 요청 시 DB에 값이 저장된다"))
    void postTest3() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());


        //when
        long count = postRepository.count();

        //then
        Assertions.assertThat(count).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회, 제목은 10자 잘라서")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        //when
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }


    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        Post post3 = Post.builder()
                .title("foo3")
                .content("bar3")
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        //when
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(3)))
                .andExpect(jsonPath("$[0].id").value(post3.getId()))
                .andExpect(jsonPath("$[0].title").value("foo3"))
                .andExpect(jsonPath("$[0].content").value("bar3"))
                .andExpect(jsonPath("$[1].id").value(post2.getId()))
                .andExpect(jsonPath("$[1].title").value("foo2"))
                .andExpect(jsonPath("$[1].content").value("bar2"))
                .andExpect(jsonPath("$[2].id").value(post1.getId()))
                .andExpect(jsonPath("$[2].title").value("foo1"))
                .andExpect(jsonPath("$[2].content").value("bar1"))
                .andDo(print());


    }

    @Test
    @DisplayName("글 페이지로 조회")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(value -> Post.builder().title("제목" + value).content("내용" + value).build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        //when
        mockMvc.perform(get("/posts?page=1&size=3&sort=id,desc")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",Matchers.is(3)))
                .andExpect(jsonPath("$[0].title").value("제목29"))
                .andExpect(jsonPath("$[0].content").value("내용29"))
                .andDo(print());


    }

    @Test
    @DisplayName("0 페이지로 조회")
    void test7() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(value -> Post.builder().title("제목" + value).content("내용" + value).build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        //when
        mockMvc.perform(get("/posts?page=0&size=3")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",Matchers.is(3)))
                .andExpect(jsonPath("$[0].title").value("제목29"))
                .andExpect(jsonPath("$[0].content").value("내용29"))
                .andDo(print());


    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("foo1")
                .content("BAR1")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        postRepository.save(post);


        //when
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test10() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        //when
        mockMvc.perform(get("/posts/{postId}", post.getId() + 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test11() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        //when
        PostEdit postEdit = PostEdit.builder()
                .title("foo1")
                .content("BAR1")
                .build();

        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId() + 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName(("내용에 바보가 들어가면 에러"))
    void postTest12() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .title("나는 바보입니다..")
                .content("내용입니당.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }


    @Test
    void test() {
        PostSearch build = PostSearch.builder()
                .build();

        System.out.println("build.getPage() = " + build.getPage());
        System.out.println("build.getSize() = " + build.getSize());
    }


}