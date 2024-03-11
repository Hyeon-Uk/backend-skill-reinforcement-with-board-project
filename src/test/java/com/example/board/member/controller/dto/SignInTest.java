package com.example.board.member.controller.dto;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.dto.auth.MemberSignIn;
import org.junit.jupiter.api.Test;

import static com.example.board.member.controller.dto.SignIn.Request;
import static com.example.board.member.controller.dto.SignIn.Response;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SignInTest {
    @Test
    void toDomain_생성_성공() {
        //given
        Request request = Request.builder()
                .id("rlagusdnr120")
                .name("Hyeonuk")
                .nickname("khu147")
                .email("rlagusdnr120@gmail.com")
                .password("Abcdefg123!")
                .passwordCheck("Abcdefg123!")
                .build();

        //when
        MemberSignIn domain = request.toDomain();

        //then
        assertAll("toDomain verification",
                () -> assertEquals("rlagusdnr120", domain.getId()),
                () -> assertEquals("Hyeonuk", domain.getName()),
                () -> assertEquals("khu147", domain.getNickname()),
                () -> assertEquals("rlagusdnr120@gmail.com", domain.getEmail()),
                () -> assertEquals("Abcdefg123!", domain.getPassword()),
                () -> assertEquals("Abcdefg123!", domain.getPasswordCheck())
        );
    }

    @Test
    void fromDomain_생성_성공() {
        //given
        Member domain = Member.builder()
                .id("rlagusdnr120")
                .name("Hyeonuk")
                .nickname("khu147")
                .email("rlagusdnr120@gmail.com")
                .password("Abcdefg123!")
                .build();

        //when
        Response response = Response.fromDomain(domain);

        //then
        assertAll("toDomain verification",
                () -> assertEquals("rlagusdnr120", domain.getId()),
                () -> assertEquals("Hyeonuk", domain.getName()),
                () -> assertEquals("khu147", domain.getNickname()),
                () -> assertEquals("rlagusdnr120@gmail.com", domain.getEmail()),
                () -> assertEquals("Abcdefg123!", domain.getPassword())
        );
    }
}