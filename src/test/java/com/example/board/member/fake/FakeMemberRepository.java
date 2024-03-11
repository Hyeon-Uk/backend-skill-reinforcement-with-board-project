package com.example.board.member.fake;

import com.example.board.member.domain.Member;
import com.example.board.member.repository.MemberRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0l);
    private final List<Member> store = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Member save(Member member) {
        if (member.getPk() == null || member.getPk() == 0) {
            Member newMember = Member.builder()
                    .pk(autoGeneratedId.incrementAndGet())
                    .email(member.getEmail())
                    .id(member.getId())
                    .password(member.getPassword())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .build();
            store.add(newMember);
            return newMember;
        } else {
            store.removeIf(item -> Objects.equals(item.getPk(), member.getPk()));
            store.add(member);
            return member;
        }
    }

    @Override
    public void delete(Member member) {
        store.removeIf(item -> Objects.equals(item.getPk(), member.getPk()));
    }

    @Override
    public Optional<Member> findByIdAndDeletedAtIsNull(long id) {
        return store.stream()
                .filter(item -> Objects.equals(item.getPk(), Long.valueOf(id)) && item.getDeletedAt() == null)
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndDeletedAtIsNull(String email) {
        return store.stream()
                .filter(item -> Objects.equals(item.getEmail(), email) && item.getDeletedAt() == null)
                .findFirst();
    }

    @Override
    public Optional<Member> findByUserIdAndDeletedAtIsNull(String id) {
        return store.stream()
                .filter(item -> Objects.equals(item.getId(), id) && item.getDeletedAt() == null)
                .findFirst();
    }
}