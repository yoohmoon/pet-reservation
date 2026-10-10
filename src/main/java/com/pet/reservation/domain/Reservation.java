package com.pet.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", unique = true)
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Lob
    private String cancelReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Reservation createReservation(Member member, Pet pet, Service service, TimeSlot timeSlot){
        Reservation reservation = new Reservation();
        reservation.member = member;
        reservation.pet = pet;
        reservation.service = service;
        reservation.timeSlot = timeSlot;
        reservation.status = ReservationStatus.PENDING;

        return reservation;
    }

    public void confirm(){
        if(this.status != ReservationStatus.PENDING){
            throw new IllegalArgumentException("승인 대기 상태에서만 확정할 수 있습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;

    }

    public void complete(){
        if(this.status != ReservationStatus.CONFIRMED){
            throw new IllegalArgumentException("확정 상태에서만 완료처리 가능합니다");
        }
        this.status = ReservationStatus.COMPLETED;
    }

    public void cancel(String reason){
        if(this.status != ReservationStatus.PENDING && this.status != ReservationStatus.CONFIRMED){
            throw new IllegalArgumentException("대기 또는 확정 상태에서만 취소할 수 있습니다.");
        }
        this.cancelReason = reason;
        this.status = ReservationStatus.CANCELED;
    }
}
