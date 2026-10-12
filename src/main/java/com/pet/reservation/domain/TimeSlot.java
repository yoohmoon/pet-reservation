package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(nullable = false)
    private LocalDate slotDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Version
    private Integer version;

    private LocalDateTime createdAt;

    public void reserve(){
        this.isAvailable = false;
    }

    public static TimeSlot createTimeSlot(Shop shop, LocalDate slotDate,
                                          LocalTime startTime,
                                          LocalTime endTime){

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.shop = shop;
        timeSlot.slotDate = slotDate;
        timeSlot.startTime = startTime;
        timeSlot.endTime = endTime;
        timeSlot.isAvailable = true;
        return timeSlot;
    }
}
