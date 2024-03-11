package com.example.board.member.controller;

import com.example.board.common.util.api.ApiUtils;
import com.example.board.member.controller.dto.SignIn;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.domain.exception.ValidationException;
import com.example.board.member.fake.FakeMemberPasswordEncrypt;
import com.example.board.member.fake.FakeMemberRepository;
import com.example.board.member.service.MemberAuthService;
import com.example.board.member.service.MemberAuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class MemberAuthControllerTest {
    MemberAuthService memberAuthService = new MemberAuthServiceImpl(new FakeMemberRepository(), new FakeMemberPasswordEncrypt());
    MemberAuthController authController = new MemberAuthController(memberAuthService);

    @Nested
    @DisplayName("[회원가입]")
    class memberSignInTest {
        @Test
        void 회원가입_성공_테스트() {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when
            ResponseEntity<ApiUtils.ApiResult<SignIn.Response>> result = authController.memberSignIn(signIn);


            //then
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            ApiUtils.ApiResult<SignIn.Response> body = result.getBody();
            assertNull(body.getError());
            assertNotNull(body.getResponse());

            SignIn.Response response = body.getResponse();
            assertAll("validation signIn's return value",
                    () -> assertEquals("rlagusdnr120", response.getId()),
                    () -> assertEquals("Hyeonuk", response.getName()),
                    () -> assertEquals("khu147", response.getNickname()),
                    () -> assertEquals("rlagusdnr120@gmail.com", response.getEmail())
            );
        }

        @Test
        void 비밀번호_비밀번호체크_불일치_테스트() {
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
            String message = assertThrows(ValidationException.class, () -> {
                authController.memberSignIn(signIn);
            }).getMessage();
            assertEquals("비밀번호가 일치하지 않습니다.", message);
        }

        @Test
        void 이메일_중복_테스트() {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            SignIn.Request duplicateEmail = SignIn.Request.builder()
                    .id("rlagusdnr1")
                    .name("Hyeonuk")
                    .nickname("khu1")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();
            authController.memberSignIn(signIn);

            //when & then
            String message = assertThrows(DuplicateException.class, () -> {
                authController.memberSignIn(duplicateEmail);
            }).getMessage();
            assertEquals("존재하는 이메일입니다.", message);
        }

        @Test
        void 아이디_중복_테스트() {
            //given
            SignIn.Request signIn = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu1")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();
            authController.memberSignIn(signIn);
            SignIn.Request duplicateId = SignIn.Request.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu1")
                    .email("rlagusdnr1@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();


            //when & then
            String message = assertThrows(DuplicateException.class, () -> {
                authController.memberSignIn(duplicateId);
            }).getMessage();
            assertEquals("존재하는 아이디입니다.", message);
        }
    }
}