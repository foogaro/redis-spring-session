package com.foogaro.controllers;

import com.foogaro.dto.Cart;
import com.foogaro.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/searchCart", method = RequestMethod.POST)
    public String cart(@RequestParam(name = "total", required = true) double total, HttpServletRequest request, Model model) {

        Iterable<Cart> carts = cartService.findAllCartTotalGreaterThan(total);
        model.addAttribute("carts", carts);
        model.addAttribute("total", total);
        return "cart";
    }

    @RequestMapping(value = "/applyDiscount", method = RequestMethod.POST)
    public String cart(@RequestParam(name = "cartId", required = true) String cartId,
                       @RequestParam(name = "total", required = true) double total,
                       @RequestParam(name = "discount", required = true) double discount,
                       HttpServletRequest request, Model model) {

        Cart cart = cartService.applyDiscount(cartId, discount);
        Iterable<Cart> carts = cartService.findAllCartTotalGreaterThan(total);
        model.addAttribute("carts", carts);
        model.addAttribute("total", total);
        return "cart";
    }

}