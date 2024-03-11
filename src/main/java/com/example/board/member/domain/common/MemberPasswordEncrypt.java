package com.example.board.member.domain.common;

public interface MemberPasswordEncrypt {
    String encrypt(String password);

    boolean match(String encrypted, String origin);
}
