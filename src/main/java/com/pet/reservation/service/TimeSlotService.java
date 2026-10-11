package com.pet.reservation.service;

import com.pet.reservation.domain.TimeSlot;
import com.pet.reservation.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public Long createTimeSlot(TimeSlot timeSlot){
        timeSlotRepository.save(timeSlot);
        return timeSlot.getId();
    }

    public TimeSlot findTimeSlot(Long id){
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 시간대입니다."));
    }
}
