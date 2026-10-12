package com.pet.reservation.controller;

import com.pet.reservation.domain.Member;
import com.pet.reservation.domain.Pet;
import com.pet.reservation.dto.CreatePetRequest;
import com.pet.reservation.service.MemberService;
import com.pet.reservation.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final MemberService memberService;
    private final PetService petService;


    @PostMapping
    public Long createPet(@RequestBody CreatePetRequest request){
        Member member = memberService.findMember(request.getMemberId());
        Pet pet = Pet.createPet(member, request.getName(), request.getSpecies(), request.getBreed(), request.getWeight(), request.getBirthDate(), request.getSpecialNotes());
        return petService.createPet(pet);
    }

    @GetMapping("/{id}")
    public Pet findPet(@PathVariable Long id){
        return petService.findPet(id);

    }
}
