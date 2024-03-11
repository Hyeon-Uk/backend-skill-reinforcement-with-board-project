package com.example.board.member.repository;

import com.example.board.member.repository.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    void delete(MemberEntity member);

    Optional<MemberEntity> findByIdAndDeletedAtIsNull(long id);

    Optional<MemberEntity> findByEmailAndDeletedAtIsNull(String email);

    Optional<MemberEntity> findByUserIdAndDeletedAtIsNull(String id);

    Optional<MemberEntity> findByNicknameAndDeletedAtIsNull(String nickname);
}
