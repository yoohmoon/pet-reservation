package com.pet.reservation.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
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

}
