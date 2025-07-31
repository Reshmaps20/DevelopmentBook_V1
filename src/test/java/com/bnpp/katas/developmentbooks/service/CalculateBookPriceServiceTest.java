package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculateBookPriceServiceTest {

    @Autowired
    private CalculateBookPriceService calculateBookPriceService;

    @Test
    @DisplayName ("Single book purchase : book price will be the same")
    public void calculatePriceForASingleBookPurchase_ShouldReturnPriceFifty() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1));

        double price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(50.0, price);
    }
    @Test
    @DisplayName ("Two different book purchase : 5% discount is applicable")
    public void calculatePriceForTwoDifferentBookPurchase_ShouldApplyFivePercentDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1));

        double price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(95.0, price);
    }
}
