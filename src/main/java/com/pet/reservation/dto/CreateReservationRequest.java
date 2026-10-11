package com.pet.reservation.dto;

import lombok.Getter;

@Getter
public class CreateReservationRequest {
    private Long memberId;
    private Long petId;
    private Long serviceId;
    private Long timeSlotId;
}
