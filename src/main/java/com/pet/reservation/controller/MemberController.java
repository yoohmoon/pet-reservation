package com.pet.reservation.controller;


import com.pet.reservation.domain.Member;
import com.pet.reservation.dto.CreateMemberRequest;
import com.pet.reservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Long createMember(@RequestBody CreateMemberRequest request){
        Member member = Member.createMember(request.getEmail(), request.getPassword(), request.getName(), request.getPhone(), request.getRole());
        return memberService.join(member);

    }
    
    @GetMapping("/{id}")
    public Member findMember(@PathVariable Long id){
        return memberService.findMember(id);
    }
}
