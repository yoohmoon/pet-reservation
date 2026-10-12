package com.pet.reservation.dto;

import com.pet.reservation.domain.Member;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CreatePetRequest {
    private Long memberId;

    private String name;
    private String species;
    private String breed;
    private BigDecimal weight;
    private LocalDate birthDate;
    private String specialNotes;
}
