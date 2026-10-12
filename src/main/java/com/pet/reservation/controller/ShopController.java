package com.pet.reservation.controller;

import com.pet.reservation.domain.Member;
import com.pet.reservation.domain.Shop;
import com.pet.reservation.dto.CreateShopRequest;
import com.pet.reservation.service.MemberService;
import com.pet.reservation.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final MemberService memberService;

    @PostMapping
    public Long createShop(@RequestBody CreateShopRequest request){
        Member owner = memberService.findMember(request.getOwnerId());

        Shop shop = Shop.createShop(owner, request.getName(), request.getPhone(), request.getAddress(), request.getDescription(), request.getOpenTime(), request.getCloseTime(), request.getClosedDays());

        return shopService.createShop(shop);
    }

    @GetMapping("/{id}")
    public Shop findShop(@PathVariable Long id){
        return shopService.findShop(id);
    }
}
