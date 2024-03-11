package com.example.board.member.repository.entity;

import com.example.board.member.domain.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberEntityTest {
    @Test
    void fromDomain_테스트() {
        //given
        Member domain = Member.builder()
                .pk(1l)
                .id("rlagusdnr120")
                .name("Hyeonuk")
                .nickname("khu147")
                .email("rlagusdnr120@gmail.com")
                .password("Password!")
                .build();

        //when
        MemberEntity entity = MemberEntity.fromDomain(domain);

        //then
        assertAll(
                "domain to entity verification",
                () -> assertEquals(1l, entity.getId()),
                () -> assertEquals("rlagusdnr120", entity.getUserId()),
                () -> assertEquals("Hyeonuk", entity.getName()),
                () -> assertEquals("khu147", entity.getNickname()),
                () -> assertEquals("rlagusdnr120@gmail.com", entity.getEmail()),
                () -> assertEquals("Password!", entity.getPassword())
        );
    }

    @Test
    void toDomain_테스트() {
        //given
        MemberEntity entity = MemberEntity.builder()
                .id(1l)
                .userId("rlagusdnr120")
                .name("Hyeonuk")
                .nickname("khu147")
                .email("rlagusdnr120@gmail.com")
                .password("Password!")
                .build();

        //when
        Member domain = entity.toDomain();

        //then
        assertAll(
                "entity to domain verification",
                () -> assertEquals(1l, domain.getPk()),
                () -> assertEquals("rlagusdnr120", domain.getId()),
                () -> assertEquals("Hyeonuk", domain.getName()),
                () -> assertEquals("khu147", domain.getNickname()),
                () -> assertEquals("rlagusdnr120@gmail.com", domain.getEmail()),
                () -> assertEquals("Password!", domain.getPassword())
        );
    }
}
