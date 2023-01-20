package com.honglog.api.service;

import com.honglog.api.domain.Post;
import com.honglog.api.repository.PostRepository;
import com.honglog.api.request.PostCreate;
import com.honglog.api.request.PostEdit;
import com.honglog.api.request.PostSearch;
import com.honglog.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        assertEquals("내용입니다.", post.getContent());

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

        PostResponse response = postService.get(savedPost.getId());

        assertNotNull(response);
        assertThat(post.getContent()).isEqualTo(response.getContent());
        assertThat(post.getTitle()).isEqualTo(response.getTitle());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given

        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build(),
                Post.builder()
                        .title("foo3")
                        .content("bar3")
                        .build()
        ));

        //when

        PostSearch postSearch = PostSearch.builder().page(1).size(3).build();


        List<PostResponse> posts = postService.getList(postSearch);

        assertThat(posts.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("페이지로 글 조회")
    void test6() {
        //given
        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(value -> Post.builder().title("제목" + value).content("내용" + value).build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder().page(1).size(5).build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        assertThat(posts.size()).isEqualTo(5);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목29");
        assertThat(posts.get(4).getTitle()).isEqualTo("제목25");


    }


    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("FOO1")
                .content("bar1")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changed = postRepository.findById(post.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(changed.getTitle()).isEqualTo("FOO1");
        assertThat(changed.getContent()).isEqualTo("bar1");

    }

    @Test
    @DisplayName("글 내용 수정")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("BAR1")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changed = postRepository.findById(post.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(changed.getTitle()).isEqualTo("foo1");
        assertThat(changed.getContent()).isEqualTo("BAR1");

    }

    @Test
    @DisplayName("글 삭제")
    void test9() {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then

        assertThat(postRepository.count()).isEqualTo(0);


    }
}