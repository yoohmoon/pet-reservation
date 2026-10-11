package com.pet.reservation.controller;

import com.pet.reservation.dto.CancelReservationRequest;
import com.pet.reservation.dto.CreateReservationRequest;
import com.pet.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public Long createReservation(@RequestBody CreateReservationRequest request){
        return reservationService.createReservation(request.getMemberId(), request.getPetId(), request.getServiceId(), request.getTimeSlotId());

    }

    @PatchMapping("/{id}/confirm")
    public void confirm(@PathVariable Long id){
        reservationService.confirmReservation(id);
    }

    @PatchMapping("/{id}/complete")
    public void complete(@PathVariable Long id){
        reservationService.completeReservation(id);
    }

    @PatchMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id, @RequestBody CancelReservationRequest request){
        reservationService.cancelReservation(id, request.getReason());
    }


}
