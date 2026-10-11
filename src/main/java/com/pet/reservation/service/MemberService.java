package com.pet.reservation.service;

import com.pet.reservation.domain.Member;
import com.pet.reservation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){

//        1. memberRepository.findByEmail(member.getEmail())로 조회
        Optional<Member> found = memberRepository.findByEmail(member.getEmail());
        if(found.isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        memberRepository.save(member);
        return member.getId();
//        2. 이미 존재하면 예외 던지기
//        3. 없으면 저장 + id 반환
    }

    public Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원입니다."));

    }
}
