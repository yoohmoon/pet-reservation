package com.pet.reservation.service;

import com.pet.reservation.domain.*;
import com.pet.reservation.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void 같은_타임슬롯에_동시에_두명이_예약하면_하나만_성공해야한다() throws InterruptedException {
        Member memberA = Member.createMember("a@test.com", "1234", "고객A", "010-1111-1111", MemberRole.CUSTOMER);
        Member memberB = Member.createMember("b@test.com", "1234", "고객B", "010-2222-2222", MemberRole.CUSTOMER);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Member memberC = Member.createMember("c@test.com", "1234", "고객C", "010-2233-3333", MemberRole.OWNER);
        memberRepository.save(memberC);

        Shop shop = Shop.createShop(memberC, "행복 펫살롱", "02-1234-5678", "서울시 강남구", "설명",
                 LocalTime.of(10, 0), LocalTime.of(18, 30),"일요일");
        shopRepository.save(shop);

        Pet petA = Pet.createPet(memberA, "콩이", "강아지", "장모치와와", new BigDecimal("4.5"), LocalDate.of(2012, 6, 12), "오리 알러지");
        Pet petB = Pet.createPet(memberB, "쪼꼬", "강아지", "토이푸들", new BigDecimal("3"), LocalDate.of(2011, 11, 12), "신장 주의");
        petRepository.save(petA);
        petRepository.save(petB);


        Service service = Service.createService(shop, "기본 미용", "전체 미용 서비스", 60, 50000);
        serviceRepository.save(service);

        TimeSlot timeSlot = TimeSlot.createTimeSlot(shop, LocalDate.of(2026, 12, 25), LocalTime.of(10, 0), LocalTime.of(11, 0));
        timeSlotRepository.save(timeSlot);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.submit(() -> {
            try {
                latch.await();
                reservationService.createReservation(memberA.getId(), petA.getId(), service.getId(), timeSlot.getId());
            } catch (Exception e) {
                System.out.println("고객 A 예약 실패" + e.getMessage());

            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                reservationService.createReservation(memberB.getId(), petB.getId(), service.getId(), timeSlot.getId());
            } catch (Exception e) {
                System.out.println("고객 B 예약 실패" + e.getMessage());

            }
        });

        latch.countDown();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        long count = reservationRepository.count();
        System.out.println("생성된 예약 수"+count);

    }
}
