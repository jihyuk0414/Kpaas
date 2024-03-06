package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MemberDto;
import org.example.dto.ResponseCustom;
import org.example.entity.Member;
import org.example.jwt.JwtDto;
import org.example.jwt.JwtProvider;
import org.example.repository.MemberRepository;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtProvider jwtProvider;
    public boolean duplicate(MemberDto memberDto){
        return memberRepository.findByEmail(memberDto.getEmail()).isPresent();
    }
    @Transactional
    public ResponseCustom join(MemberDto memberDto){
        if (duplicate(memberDto)){
            return ResponseCustom.builder()
                    .message("이미 가입되어 있는 회원입니다.")
                    .state("중복된 이메일")
                    .build();
        }
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        Member member = Member.builder()
                .memberDto(memberDto)
                .build();
        memberRepository.save(member);
        return ResponseCustom.builder()
                .message("회원가입 되었습니다")
                .state("처리 성공")
                .build();
    }
    public JwtDto login(MemberDto memberDto){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberDto.getEmail(),memberDto.getPassword());
        Authentication auth = authenticationProvider.authenticate(token);
        return jwtProvider.createToken(auth);
    }
    public List<Member> getOne(){
        return memberRepository.findAll();
    }
    public int getPoint(String email, int point){

        if (memberRepository.findByEmail(email).isPresent()){
            Member member=memberRepository.findByEmail(email).get();
            return member.getPoint()+point;
        }
        return 0;
    }


}
