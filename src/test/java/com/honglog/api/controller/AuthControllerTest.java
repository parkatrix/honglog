package com.honglog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honglog.api.domain.Session;
import com.honglog.api.domain.User;
import com.honglog.api.repository.SessionRepository;
import com.honglog.api.repository.UserRepository;
import com.honglog.api.request.Login;
import com.honglog.api.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName(("로그인 성공"))
    void test01() throws Exception {


        userRepository.save(User.builder().email("parkatrix@gmail.com").password("1234").build());


        Login login = Login.builder().email("parkatrix@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);


        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(("로그인 성공후 세션 응답"))
    void test02() throws Exception {
        User savedUser = User.builder().email("parkatrix@gmail.com").password("1234").build();

        userRepository.save(savedUser);


        Login login = Login.builder().email("parkatrix@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);


        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

        User findUser = userRepository.findById(savedUser.getId()).orElseThrow();

        Assertions.assertEquals(1L, findUser.getSessions().size());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지를 접속한다 /foo")
    void test03() throws Exception {

        User user = User.builder()
                .email("parkatrix@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);


        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test04() throws Exception {

        User user = User.builder()
                .email("parkatrix@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);


        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken()+ "asdf")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


}