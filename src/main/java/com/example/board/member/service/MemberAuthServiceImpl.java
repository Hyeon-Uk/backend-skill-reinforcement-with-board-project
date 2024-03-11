package com.example.board.member.service;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.common.MemberPasswordEncrypt;
import com.example.board.member.domain.dto.auth.MemberSignIn;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.domain.exception.ValidationException;
import com.example.board.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {
    private final MemberRepository memberRepository;
    private final MemberPasswordEncrypt memberPasswordEncrypt;

    @Override
    @Transactional
    public Member signIn(MemberSignIn signIn) {
        if (!signIn.getPassword().equals(signIn.getPasswordCheck())) {
            throw new ValidationException("비밀번호가 일치하지 않습니다.");
        }

        Optional<Member> emailDuplicateResult = memberRepository.findByEmailAndDeletedAtIsNull(signIn.getEmail());
        if (emailDuplicateResult.isPresent()) {
            throw new DuplicateException("존재하는 이메일입니다.");
        }

        Optional<Member> idDuplicateResult = memberRepository.findByUserIdAndDeletedAtIsNull(signIn.getId());
        if (idDuplicateResult.isPresent()) {
            throw new DuplicateException("존재하는 아이디입니다.");
        }

        //비밀번호 암호화하여 저장
        Member member = signIn.toDomainWithEncryptPassword(memberPasswordEncrypt);

        member = memberRepository.save(member);
        return member;
    }
}
