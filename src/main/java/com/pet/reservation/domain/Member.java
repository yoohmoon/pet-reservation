package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Member createMember(String email, String password, String name, String phone, MemberRole role){
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.name = name;
        member.phone = phone;
        member.role = role;

        return member;
    }
}
