package com.pet.reservation.service;

import com.pet.reservation.domain.Pet;
import com.pet.reservation.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public Long createPet(Pet pet){
        petRepository.save(pet);
        return pet.getId();
    }

    public Pet findPet(Long id){
        return petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));
    }

}
