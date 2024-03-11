package com.example.board.integration.member.controller;

import com.example.board.member.controller.dto.SignIn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class MemberAuthControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    public String objectToJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }


    @Nested
    @DisplayName("[회원가입]")
    class MemberSignInTest {
        @Test
        void 회원가입_성공_테스트() throws Exception {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when & then
            mvc.perform(post("/api/member/auth/signin")
                            .content(objectToJsonString(signIn))
                            .contentType("application/json")
                            .characterEncoding("utf8"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.error").isEmpty())
                    .andExpect(jsonPath("$.response.id").value(signIn.getId()))
                    .andExpect(jsonPath("$.response.email").value(signIn.getEmail()))
                    .andExpect(jsonPath("$.response.nickname").value(signIn.getNickname()))
                    .andExpect(jsonPath("$.response.name").value(signIn.getName()));
        }

        @Test
        void 비밀번호_비밀번호체크_불일치_테스트() throws Exception {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("notEquals!")
                    .build();

            //when & then
            mvc.perform(post("/api/member/auth/signin")
                            .content(objectToJsonString(signIn))
                            .contentType("application/json")
                            .characterEncoding("utf8"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.response").isEmpty())
                    .andExpect(jsonPath("$.error.message").value("비밀번호가 일치하지 않습니다."));
        }

        @Test
        void 이메일_중복_테스트() throws Exception {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();
            mvc.perform(post("/api/member/auth/signin")
                    .content(objectToJsonString(signIn))
                    .contentType("application/json")
                    .characterEncoding("utf8"));

            SignIn.Request duplicateEmail = SignIn.Request.builder()
                    .id("rlagusdnr1")
                    .name("Hyeonuk")
                    .nickname("khu1")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when & then
            mvc.perform(post("/api/member/auth/signin")
                            .content(objectToJsonString(duplicateEmail))
                            .contentType("application/json")
                            .characterEncoding("utf8"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.response").isEmpty())
                    .andExpect(jsonPath("$.error.message").value("존재하는 이메일입니다."));
        }

        @Test
        void 아이디_중복_테스트() throws Exception {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu1")
                    .email("rlagusdnr1@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();
            mvc.perform(post("/api/member/auth/signin")
                    .content(objectToJsonString(signIn))
                    .contentType("application/json")
                    .characterEncoding("utf8"));

            SignIn.Request duplicateId = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            mvc.perform(post("/api/member/auth/signin")
                            .content(objectToJsonString(duplicateId))
                            .contentType("application/json")
                            .characterEncoding("utf8"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.response").isEmpty())
                    .andExpect(jsonPath("$.error.message").value("존재하는 아이디입니다."));
        }
    }
}
