package com.pet.reservation.controller;

import com.pet.reservation.domain.Shop;
import com.pet.reservation.domain.TimeSlot;
import com.pet.reservation.dto.CreateTimeSlotRequest;
import com.pet.reservation.service.ShopService;
import com.pet.reservation.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
public class TimeSlotController {

    private final ShopService shopService;
    private final TimeSlotService timeSlotService;


    @PostMapping
    public Long createTimeSlot(@RequestBody CreateTimeSlotRequest request){
        Shop shop = shopService.findShop(request.getShopId());
        TimeSlot timeSlot = TimeSlot.createTimeSlot(shop, request.getSlotDate(), request.getStartTime(), request.getEndTime());
       return timeSlotService.createTimeSlot(timeSlot);

    }

    @GetMapping("/{id}")
    public TimeSlot findTimeSlot(@PathVariable Long id){
        return timeSlotService.findTimeSlot(id);
    }
}
