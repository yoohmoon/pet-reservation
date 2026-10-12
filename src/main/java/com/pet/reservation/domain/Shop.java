package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Lob
    private String description;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Column(length = 50)
    private String closedDays;

    private LocalDateTime createdAt;

    public static Shop createShop(Member owner, String name, String phone, String address, String description, LocalTime openTime, LocalTime closeTime, String closedDays){
        Shop shop = new Shop();
        shop.owner = owner;
        shop.name = name;
        shop.phone = phone;
        shop.address = address;
        shop.description = description;
        shop.openTime = openTime;
        shop.closeTime = closeTime;
        shop.closedDays = closedDays;
        return shop;
    }
}
