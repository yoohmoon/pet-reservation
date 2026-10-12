package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20, nullable = false)
    private String species;

    @Column(length = 50)
    private String breed;

    @Column(precision = 4, scale = 1)
    private BigDecimal weight;

    private LocalDate birthDate;

    @Lob
    private String specialNotes;

    private LocalDateTime createdAt;

    public static Pet createPet(Member member, String name,
                                String species,  String breed,
                                BigDecimal weight
    ,LocalDate birthDate, String specialNotes){
        Pet pet = new Pet();
        pet.member = member;
        pet.name = name;
        pet.species = species;
        pet.breed = breed;
        pet.weight = weight;
        pet.birthDate = birthDate;
        pet.specialNotes= specialNotes;
        return pet;
    }
}
