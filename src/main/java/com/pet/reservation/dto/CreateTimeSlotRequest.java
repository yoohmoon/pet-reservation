package com.pet.reservation.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CreateTimeSlotRequest {
    private Long shopId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
