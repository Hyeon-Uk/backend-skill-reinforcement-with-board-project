package com.example.board.member.controller.dto;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.dto.auth.MemberSignIn;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SignIn {
    private SignIn() {
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Setter
    public static class Request {
        private String id;
        private String email;
        private String nickname;
        private String name;
        private String password;
        @JsonProperty(value = "password_check")
        private String passwordCheck;

        public MemberSignIn toDomain() {
            return MemberSignIn.builder()
                    .id(this.getId())
                    .password(this.getPassword())
                    .passwordCheck(this.getPasswordCheck())
                    .name(this.getName())
                    .nickname(this.getNickname())
                    .email(this.getEmail())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String id;
        private String email;
        private String name;
        private String nickname;

        public static Response fromDomain(Member domain) {
            return Response.builder()
                    .id(domain.getId())
                    .email(domain.getEmail())
                    .name(domain.getName())
                    .nickname(domain.getNickname())
                    .build();
        }
    }
}
