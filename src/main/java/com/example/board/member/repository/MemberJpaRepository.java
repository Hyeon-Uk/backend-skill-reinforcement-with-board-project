package com.example.board.member.repository;

import com.example.board.member.repository.entity.MemberEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    void delete(MemberEntity member);

    Optional<MemberEntity> findByIdAndDeletedAtIsNull(long id);

    Optional<MemberEntity> findByEmailAndDeletedAtIsNull(String email);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<MemberEntity> findByUserIdAndDeletedAtIsNull(String id);
}
