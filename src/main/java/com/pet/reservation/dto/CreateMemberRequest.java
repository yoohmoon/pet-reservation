package com.pet.reservation.dto;

import com.pet.reservation.domain.MemberRole;
import lombok.Getter;

@Getter
public class CreateMemberRequest {
    private String email;

    private String password;

    private String name;

    private String phone;

    private MemberRole role;
}
