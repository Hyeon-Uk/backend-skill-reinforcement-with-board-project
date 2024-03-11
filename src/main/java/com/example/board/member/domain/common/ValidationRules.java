package com.example.board.member.domain.common;

public enum ValidationRules {
    ID("^.{8,20}$", "ID는 8~20자여야 합니다."),
    PASSWORD("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$", "비밀번호는 영문, 숫자, 특수기호를 각각 1번 이상 사용하며 8~16자여야 합니다."),
    EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", "올바른 이메일 형식이 아닙니다."),
    NAME("^[가-힣a-zA-Z]{2,10}$", "이름은 2~10자의 한글 또는 영문이어야 합니다."),
    NICKNAME("^[a-zA-Z0-9]{3,20}$", "닉네임은 3~20자의 영문 대소문자와 숫자의 조합이어야 합니다.");

    private final String pattern;
    private final String errorMessage;

    ValidationRules(String pattern, String errorMessage) {
        this.pattern = pattern;
        this.errorMessage = errorMessage;
    }

    public String getPattern() {
        return pattern;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
