package com.foogaro.repositories;

import com.foogaro.dto.Cart;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;

public interface CartRepository extends RedisDocumentRepository<Cart, String> {

    Optional<Cart> findOneByUserId(String userId);

}