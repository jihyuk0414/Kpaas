package org.example.controller;

import org.example.dto.MemberDto;
import org.example.dto.ResponseCustom;
import org.example.entity.Member;
import org.example.jwt.JwtDto;
import org.example.service.kakao.KakaoService;
import org.example.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseCustom> signup(@RequestBody MemberDto memberDto){
        return ResponseEntity.ok(memberService.join(memberDto));
    }
    @PostMapping("/login")
    public ResponseEntity<JwtDto> follow(@RequestBody MemberDto memberDto){
        JwtDto jwtDto = memberService.login(memberDto);
        return ResponseEntity.ok(jwtDto);
    }
    @GetMapping("/info")
    public List<String> info(){
        return memberService.getOne().stream().map(Member::getEmail).collect(Collectors.toList());

    }
    @PostMapping("/point")
    public int changePoint(@RequestBody int point, @RequestBody String email){
        return memberService.getPoint(email,point);
    }
    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=b9759cba8e0cdd5bcdb9d601f5a10ac1&redirect_uri=http://localhost:8080/oauth2/kakao
}
