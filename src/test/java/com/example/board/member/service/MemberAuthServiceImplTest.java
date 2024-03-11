package com.example.board.member.service;


import com.example.board.member.domain.Member;
import com.example.board.member.domain.common.MemberPasswordEncrypt;
import com.example.board.member.domain.dto.auth.MemberSignIn;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.domain.exception.ValidationException;
import com.example.board.member.fake.FakeMemberPasswordEncrypt;
import com.example.board.member.fake.FakeMemberRepository;
import com.example.board.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberAuthServiceImplTest {
    private MemberRepository memberRepository = new FakeMemberRepository();
    private MemberPasswordEncrypt memberPasswordEncrypt = new FakeMemberPasswordEncrypt();
    private MemberAuthService authService = new MemberAuthServiceImpl(memberRepository, memberPasswordEncrypt);

    @Nested
    @DisplayName("[회원가입]")
    class SignInTest {
        @Test
        void 회원가입_성공_테스트() {
            //given
            MemberSignIn signIn = MemberSignIn.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when
            Member member = authService.signIn(signIn);

            //then
            assertAll("validation signIn's return value",
                    () -> assertEquals("rlagusdnr120", member.getId()),
                    () -> assertEquals("Hyeonuk", member.getName()),
                    () -> assertEquals("khu147", member.getNickname()),
                    () -> assertEquals("rlagusdnr120@gmail.com", member.getEmail()),
                    () -> assertTrue(memberPasswordEncrypt.match(member.getPassword(), "Abcdefg123!"))
            );
        }

        @Test
        void 비밀번호_비밀번호체크_불일치_테스트() {
            //given
            MemberSignIn signIn = MemberSignIn.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("notEqualsPwd123!")
                    .build();

            //when & then
            String message = assertThrows(ValidationException.class, () -> {
                authService.signIn(signIn);
            }).getMessage();
            assertEquals("비밀번호가 일치하지 않습니다.", message);
        }

        @Test
        void 이메일_중복_테스트() {
            //given
            memberRepository.save(Member.builder()
                    .id("rlagusdnr1")
                    .name("Hyeon")
                    .nickname("khu1")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .build());

            MemberSignIn signIn = MemberSignIn.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when & then
            String message = assertThrows(DuplicateException.class, () -> {
                authService.signIn(signIn);
            }).getMessage();
            assertEquals("존재하는 이메일입니다.", message);
        }

        @Test
        void 아이디_중복_테스트() {
            //given
            memberRepository.save(Member.builder()
                    .id("rlagusdnr120")
                    .name("Hyeon")
                    .nickname("khu1")
                    .email("rlagusdnr1@gmail.com")
                    .password("Abcdefg123!")
                    .build());

            MemberSignIn signIn = MemberSignIn.builder()
                    .id("rlagusdnr120")
                    .name("Hyeonuk")
                    .nickname("khu147")
                    .email("rlagusdnr120@gmail.com")
                    .password("Abcdefg123!")
                    .passwordCheck("Abcdefg123!")
                    .build();

            //when & then
            String message = assertThrows(DuplicateException.class, () -> {
                authService.signIn(signIn);
            }).getMessage();
            assertEquals("존재하는 아이디입니다.", message);
        }
    }
}