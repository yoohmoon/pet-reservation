package com.pet.reservation.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateShopRequest {
    private Long ownerId;
    private String name;
    private String phone;
    private String address;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String closedDays;
}
