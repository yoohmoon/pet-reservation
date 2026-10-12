package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Service {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(length = 100, nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer price;

    private LocalDateTime createdAt;

    public static Service createService(Shop shop, String name, String description,
                                        Integer duration, Integer price){
        Service service = new Service();
        service.shop = shop;
        service.name = name;
        service.description = description;
        service.duration = duration;
        service.price = price;

        return service;
    }
}
