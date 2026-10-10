package com.pet.reservation.service;

import com.pet.reservation.domain.Shop;
import com.pet.reservation.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    @Transactional
    public Long createShop(Shop shop){
        shopRepository.save(shop);
        return shop.getId();
    }

    public Shop findShop(Long id){
       return shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다."));
    }
}
