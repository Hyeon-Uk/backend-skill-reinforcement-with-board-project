package com.example.board.member.domain.dto.auth;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.common.MemberPasswordEncrypt;
import com.example.board.member.domain.exception.ValidationException;
import lombok.Builder;
import lombok.Getter;

import java.util.regex.Pattern;

import static com.example.board.member.domain.common.ValidationRules.*;

@Getter
public class MemberSignIn {
    private String id;
    private String email;
    private String password;
    private String passwordCheck;
    private String name;
    private String nickname;

    @Builder
    public MemberSignIn(String id, String email, String password, String passwordCheck, String name, String nickname) {
        setId(id);
        setEmail(email);
        setPassword(password);
        setPasswordCheck(passwordCheck);
        setName(name);
        setNickname(nickname);
    }

    public void setId(String id) {
        if (id == null || !Pattern.matches(ID.getPattern(), id)) {
            throw new ValidationException(ID.getErrorMessage());
        }
        this.id = id;
    }

    public void setEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL.getPattern(), email)) {
            throw new ValidationException(EMAIL.getErrorMessage());
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || !Pattern.matches(PASSWORD.getPattern(), password)) {
            throw new ValidationException(PASSWORD.getErrorMessage());
        }
        this.password = password;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public void setName(String name) {
        if (name == null || !Pattern.matches(NAME.getPattern(), name)) {
            throw new ValidationException(NAME.getErrorMessage());
        }
        this.name = name;
    }

    public void setNickname(String nickname) {
        if (nickname == null || !Pattern.matches(NICKNAME.getPattern(), nickname)) {
            throw new ValidationException(NICKNAME.getErrorMessage());
        }
        this.nickname = nickname;
    }

    public Member toDomainWithEncryptPassword(MemberPasswordEncrypt pe) {
        return Member.builder().id(this.id).email(this.email).name(this.name).nickname(this.nickname).password(pe.encrypt(this.password)).build();
    }
}
