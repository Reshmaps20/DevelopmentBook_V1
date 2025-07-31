package com.bnpp.katas.developmentbooks.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculateBookPriceServiceTest {

    @Test
    public void calculatePriceForASingleBookPurchase_ShouldReturnPriceFifty() {

        CalculateBookPriceService calculateBookPriceService = new CalculateBookPriceService();

        double price = calculateBookPriceService.calculatePrice(1,1);

        assertEquals(50.0, price);
    }
}
