package com.foogaro.services;

import com.foogaro.dto.Cart;
import com.foogaro.dto.Cart$;
import com.foogaro.repositories.CartRepository;
import com.redis.om.spring.search.stream.EntityStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.search.aggr.SortedField;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private EntityStream entityStream;

    @Autowired
    private CartRepository cartRepository;

    public Iterable<Cart> findAllCartTotalGreaterThan(final double total) {
        return entityStream
                .of(Cart.class)
                .filter(Cart$.TOTAL.ge(total))
                .sorted(Cart$.TOTAL, SortedField.SortOrder.ASC)
                .collect(Collectors.toList());
    }

    public Cart findById(final String id) {
        return cartRepository.findById(id).get();
    }

    public Cart applyDiscount(final String id, double discount) {
        Cart cart = findById(id);
        if (cart != null) {
            cart.setDiscount(discount);
            cart.setTotal(cart.getTotal() - cart.getDiscount());
            cart = cartRepository.save(cart);
            return cart;
        } else {
            return null;
        }
    }
}
