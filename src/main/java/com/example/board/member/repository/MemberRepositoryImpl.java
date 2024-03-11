package com.example.board.member.repository;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.repository.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    @Transactional
    public Member save(Member member) {
        try {
            member = memberJpaRepository
                    .save(MemberEntity.fromDomain(member))
                    .toDomain();
            return member;
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            String message = e.getMessage().toLowerCase();

            if (message.contains("unique")) {
                if (message.contains("member_id")) {
                    throw new DuplicateException("존재하는 아이디입니다.");
                } else if (message.contains("member_email")) {
                    throw new DuplicateException("존재하는 이메일입니다.");
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    @Override
    public void delete(Member member) {
        memberJpaRepository.delete(MemberEntity.fromDomain(member));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByIdAndDeletedAtIsNull(long id) {
        return memberJpaRepository
                .findByIdAndDeletedAtIsNull(id)
                .map(MemberEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByEmailAndDeletedAtIsNull(String email) {
        return memberJpaRepository
                .findByEmailAndDeletedAtIsNull(email)
                .map(MemberEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByUserIdAndDeletedAtIsNull(String id) {
        return memberJpaRepository
                .findByUserIdAndDeletedAtIsNull(id)
                .map(MemberEntity::toDomain);
    }
}
