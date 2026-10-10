package com.pet.reservation.service;

import com.pet.reservation.domain.Member;
import com.pet.reservation.domain.Pet;
import com.pet.reservation.domain.Reservation;
import com.pet.reservation.domain.TimeSlot;
import com.pet.reservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public Long createReservation(Long memberId, Long petId, Long serviceId, Long timeSlotId) {
        // 1. 각 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("반려동물을 찾을 수 없습니다."));
        com.pet.reservation.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("서비스를 찾을 수 없습니다."));
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("타임슬롯을 찾을 수 없습니다."));
        // 2. timeSlot.isAvailable이 false면 예외 던지기
        if (!timeSlot.getIsAvailable()) {
            throw new IllegalArgumentException("이미 예약된 시간대입니다.");
        }
        // 3. Reservation 생성 + 저장

        timeSlot.reserve();
        Reservation reservation = Reservation.createReservation(member, pet, service, timeSlot);
        reservationRepository.save(reservation);
        // 4. 생성된 예약 id 반환

        return reservation.getId();
    }

    @Transactional
    public void confirmReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.confirm();

    }

    @Transactional
    public void completeReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.complete();
    }

    @Transactional
    public void cancelReservation(Long reservationId, String reason){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.cancel(reason);
    }

}
