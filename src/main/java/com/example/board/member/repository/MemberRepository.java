package com.example.board.member.repository;

import com.example.board.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    void delete(Member member);

    Optional<Member> findByIdAndDeletedAtIsNull(long id);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByUserIdAndDeletedAtIsNull(String id);

    Optional<Member> findByNicknameAndDeletedAtIsNull(String nickname);
}
