package com.example.board.member.domain.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberPasswordEncryptTest {
    private MemberPasswordEncrypt memberPasswordEncrypt = new MemberPasswordEncryptImpl();

    @Test
    void encrypt() {
        String password = "rlagusdnr120";
        String encrypt = memberPasswordEncrypt.encrypt(password);
        assertNotEquals(encrypt, password);
    }

    @Test
    void match() {
        String password = "rlagusdnr120";
        String encrypt = memberPasswordEncrypt.encrypt(password);
        System.out.println("encrypt = " + encrypt);
        assertTrue(memberPasswordEncrypt.match(encrypt, password));

        assertFalse(memberPasswordEncrypt.match(encrypt, "otherpassword!"));
    }
}