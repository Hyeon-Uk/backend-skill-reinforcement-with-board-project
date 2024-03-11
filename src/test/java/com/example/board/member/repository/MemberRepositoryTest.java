package com.example.board.member.repository;

import com.example.board.member.repository.entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    private MemberJpaRepository memberRepository;

    @Test
    void CREATED_AT_적용되는지_테스트() {
        //given
        MemberEntity memberEntity = MemberEntity.builder().userId("rlagusdnr120").name("Hyeonuk").nickname("khu147").email("rlagusdnr120@gmail.com").password("abcdefg123!").build();
        assertNull(memberEntity.getCreatedAt());

        //when
        memberRepository.save(memberEntity);

        //then
        assertNotNull(memberEntity.getCreatedAt());
    }

    @Test
    void UPDATED_AT_적용되는지_테스트() {
        //given
        MemberEntity memberEntity = MemberEntity.builder().userId("rlagusdnr120").name("Hyeonuk").nickname("khu147").email("rlagusdnr120@gmail.com").password("abcdefg123!").build();

        //when & then
        memberRepository.save(memberEntity);
        assertNotNull(memberEntity.getUpdatedAt());
        LocalDateTime beforeUpdate = memberEntity.getUpdatedAt();

        memberEntity.changeNickname("change1");
        memberRepository.saveAndFlush(memberEntity);

        LocalDateTime afterUpdate = memberEntity.getUpdatedAt();

        assertNotEquals(beforeUpdate, afterUpdate);
    }

    @Test
    void DELETED_AT_적용되는지_테스트() {
        //given
        MemberEntity memberEntity = MemberEntity.builder().userId("rlagusdnr120").name("Hyeonuk").nickname("khu147").email("rlagusdnr120@gmail.com").password("abcdefg123!").build();
        memberRepository.save(memberEntity);

        //when
        memberRepository.delete(memberEntity);

        //then
        assertEquals(1, memberRepository.findAll().size());//데이터베이스에는 물리적으로 남아있지만
        assertFalse(memberRepository.findByIdAndDeletedAtIsNull(memberEntity.getId()).isPresent());//deletedAt이 null이 아닌 논리적인 삭제상태기 때문에 찾을 수 없음
    }
}