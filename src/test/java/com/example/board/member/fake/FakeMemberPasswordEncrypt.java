package com.example.board.member.fake;

import com.example.board.member.domain.common.MemberPasswordEncrypt;

public class FakeMemberPasswordEncrypt implements MemberPasswordEncrypt {
    @Override
    public String encrypt(String password) {
        StringBuilder sb = new StringBuilder();
        return sb.append(password).reverse().toString();
    }

    @Override
    public boolean match(String encrypted, String origin) {
        StringBuilder sb = new StringBuilder();
        return sb.append(origin).reverse().toString().equals(encrypted);
    }
}
