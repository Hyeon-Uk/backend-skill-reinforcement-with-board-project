package com.example.board.member.domain.dto.auth;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.common.MemberPasswordEncrypt;
import com.example.board.member.domain.exception.ValidationException;
import com.example.board.member.fake.FakeMemberPasswordEncrypt;
import org.junit.jupiter.api.Test;

import static com.example.board.member.domain.common.ValidationRules.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberSignInTest {
    @Test
    void 모든_필드_유효성_검사_성공() {
        assertDoesNotThrow(
                () ->
                        MemberSignIn
                                .builder()
                                .id("rlagusdnr120")
                                .email("rlagusdnr120@gmail.com")
                                .password("Abcdefg123!")
                                .name("KimHyeonuk")
                                .nickname("khu147")
                                .build()
        );
    }

    @Test
    void 아이디_길이가_8_미만이면_유효성_검사_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusd")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcdefg123!")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(ID.getErrorMessage(), message);
    }

    @Test
    void 아이디_길이가_20_초과는_유효성_검사_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("helloworldhelloworld123")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcdefg123!")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(ID.getErrorMessage(), message);
    }

    @Test
    void 비밀번호_길이는_맞지만_특수문자가_안들어가면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcdefg1234")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(PASSWORD.getErrorMessage(), message);
    }

    @Test
    void 비밀번호_길이는_맞지만_영어가_안들어가면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("123456789!")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(PASSWORD.getErrorMessage(), message);
    }

    @Test
    void 비밀번호_길이는_맞지만_숫자가_안들어가면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcdefg!")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(PASSWORD.getErrorMessage(), message);
    }

    @Test
    void 비밀번호_조건은_만족하지만_길이가_8_미만이면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd12!")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(PASSWORD.getErrorMessage(), message);
    }

    @Test
    void 비밀번호_조건은_만족하지만_길이가_16_초과면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!Abcd123!@")
                        .name("KimHyeonuk")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(PASSWORD.getErrorMessage(), message);
    }

    @Test
    void 이메일_형식이_아니면_실패() {
        String message = assertThrows(ValidationException.class, () -> {
            MemberSignIn signIn = MemberSignIn
                    .builder()
                    .id("rlagusdnr120")
                    .email("rlagusdnr120")
                    .password("Abcd123!")
                    .name("KimHyeonuk")
                    .nickname("khu147")
                    .build();
        }).getMessage();
        assertEquals(EMAIL.getErrorMessage(), message);
    }

    @Test
    void 이름에_특수문자가_포함되면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("KimHyeonuk!")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(NAME.getErrorMessage(), message);
    }

    @Test
    void 이름이_2자_미만이면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("K")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(NAME.getErrorMessage(), message);
    }

    @Test
    void 이름이_10자_초과면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("KimSanandress")
                        .nickname("khu147")
                        .build()
        ).getMessage();
        assertEquals(NAME.getErrorMessage(), message);
    }

    @Test
    void 닉네임의_길이가_3글자_미만이면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("KimHyeonuk")
                        .nickname("k1")
                        .build()
        ).getMessage();
        assertEquals(NICKNAME.getErrorMessage(), message);
    }

    @Test
    void 닉네임의_길이가_20글자_초과면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("KimHyeonuk")
                        .nickname("k1k1k1k1k1k1k1k1k1k11")
                        .build()
        ).getMessage();
        assertEquals(NICKNAME.getErrorMessage(), message);
    }

    @Test
    void 닉네임에_한글이_들어가면_실패() {
        String message = assertThrows(ValidationException.class, () ->
                MemberSignIn
                        .builder()
                        .id("rlagusdnr120")
                        .email("rlagusdnr120@gmail.com")
                        .password("Abcd123!")
                        .name("KimHyeonuk")
                        .nickname("김현욱")
                        .build()
        ).getMessage();
        assertEquals(NICKNAME.getErrorMessage(), message);
    }

    @Test
    void Member로_변경시_password는_암호화_되어야_한다() {
        //given
        MemberSignIn signIn = MemberSignIn
                .builder()
                .id("rlagusdnr120")
                .email("rlagusdnr120@gmail.com")
                .password("Abcd123!")
                .name("KimHyeonuk")
                .nickname("khu147")
                .build();

        //when
        MemberPasswordEncrypt memberPasswordEncrypt = new FakeMemberPasswordEncrypt();
        Member member = signIn.toDomainWithEncryptPassword(memberPasswordEncrypt);

        //then
        assertNotEquals("Abcd123!", member.getPassword());
        assertTrue(memberPasswordEncrypt.match(member.getPassword(), "Abcd123!"));
    }
}