package com.example.board.member.service;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.dto.auth.MemberSignIn;

public interface MemberAuthService {
    Member signIn(MemberSignIn signIn);
}
