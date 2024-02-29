package com.foogaro;

import com.foogaro.dto.Cart;
import com.foogaro.dto.Product;
import com.foogaro.repositories.CartRepository;
import com.github.javafaker.Faker;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableRedisDocumentRepositories
public class Application {

    @Autowired
    CartRepository cartRepository;

    @Bean
    CommandLineRunner loadTestData() {
        return args -> {
            cartRepository.deleteAll();
            int numberOfCarts = 10;
            for (int i = 0; i < numberOfCarts; i++) cartRepository.save(getCart());
        };
    }

    private Cart getCart() {
        Cart cart = new Cart();
        Faker faker = new Faker();
        cart.setId(faker.code().gtin8());
        cart.setSessionId(faker.internet().uuid());
        cart.setUserId(faker.internet().emailAddress());
        cart.setProducts(getProductList(faker.number().numberBetween(1, 10)));
        cart.setTotalProducts(cart.getProducts().size());
        cart.setTotalQuantity(cart.getProducts().stream().mapToLong(Product::getQuantity).sum());
        cart.setTotal(cart.getProducts().stream().mapToDouble(Product::getTotal).sum());
        return cart;
    }
    private List<Product> getProductList(int numberOfProducts) {
        List<Product> products = new ArrayList<>(numberOfProducts);
        Product product;
        Faker faker = new Faker();
        for (int i = 0; i < numberOfProducts; i++) {
            product = new Product();
            product.setId(faker.code().gtin8());
            product.setTitle(faker.lordOfTheRings().character());
            product.setDescription(faker.lordOfTheRings().location());
            product.setThumbnailUrl(faker.internet().url());
            product.setPrice(faker.number().randomDouble(2, 1, 100));
            product.setQuantity(faker.number().numberBetween(1, 10));
            product.setTotal(product.getPrice() * product.getQuantity());
            products.add(product);
        }
        return products;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
