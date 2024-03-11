package com.example.board.member.controller;

import com.example.board.member.controller.dto.SignIn;
import com.example.board.member.domain.Member;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.domain.exception.ValidationException;
import com.example.board.member.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.board.common.util.api.ApiUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/auth")
public class MemberAuthController {
    private final MemberAuthService memberAuthService;

    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResult<SignIn.Response>> memberSignIn(@RequestBody SignIn.Request signIn) {
        Member member = memberAuthService.signIn(signIn.toDomain());
        return success(SignIn.Response.fromDomain(member), HttpStatus.CREATED);
    }

    @ExceptionHandler({DuplicateException.class, ValidationException.class})
    public ResponseEntity<ApiResult<Void>> badRequestHandler(Exception e) {
        return error(e, HttpStatus.BAD_REQUEST);
    }
}
