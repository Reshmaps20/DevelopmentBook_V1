package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.exception.InvalidBookException;
import com.bnpp.katas.developmentbooks.model.BookGroup;
import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.model.BookResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CalculateBookPriceServiceTest {

    @Autowired
    private CalculateBookPriceService calculateBookPriceService;

    @Test
    @DisplayName ("Single book purchase : book price will be the same")
    public void calculatePriceForASingleBookPurchase_ShouldReturnPriceFifty() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(50.0, price.getFinalPrice ());
    }
    @Test
    @DisplayName ("Two different book purchase with single copy: 5% discount is applicable")
    public void calculatePriceForTwoDifferentBookPurchase_ShouldApplyFivePercentDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(95.0, price.getFinalPrice ());
    }

    @Test
    @DisplayName ("Three different book purchase with single copy : 10% discount is applicable")
    public void calculatePriceForThreeDifferentBookPurchase_ShouldGive10PercentDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1),
                new BookRequest(3, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(135.0, price.getFinalPrice ());
    }

    @Test
    @DisplayName ("Three different book purchase with multiple copies ")
    public void CalculatePricForThreeDifferentBookWithMutlipleCopies_RetrunsMinimumTotalPrice() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 2),new BookRequest(2, 2),
                new BookRequest(3, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(230.0, price.getFinalPrice ());
    }

    @Test
    @DisplayName ("Four different book purchase with single copy: 20% discount is applicable ")
    public void calculatePriceForFourDifferentBookPurchase_ShouldGive20PercentDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1),
                new BookRequest(3, 1), new BookRequest(4, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(160.0, price.getFinalPrice ());
    }

    @Test
    @DisplayName ("Five different book purchase with single copy : 25% discount is applicable ")
    public void calculatePriceForFiveDifferentBookPurchase_ShouldGive25PercentDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1),
                new BookRequest(3, 1), new BookRequest(4, 1), new BookRequest(5, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(187.5, price.getFinalPrice ());
    }

    @Test
    @DisplayName("Five different book purchase with multiple copies")
    public void calculatePriceForMultipleCopiesOfAllTheFiveBooks_ReturnsMinimumTotalPrice() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 2),new BookRequest(2, 2),
                new BookRequest(3, 2), new BookRequest(4, 1), new BookRequest(5, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(320.0, price.getFinalPrice ());
    }

    @Test
    @DisplayName ("Calculate actual price , total discount and total price")
    public void calculatePriceForDistinctBooks_ShouldCalculateFinalPriceAndDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);

        assertEquals(100.0, price.getActualPrice ());
        assertEquals(5.0, price.getTotalDiscount ());
        assertEquals(95.0, price.getFinalPrice ());
        assertEquals(2, price.getListOfBookGroups ().get (0).getNumberOfBooks ());
        assertEquals(5.0, price.getListOfBookGroups ().get (0).getDiscountAmount ());
    }

    @Test
    @DisplayName ("Calculate each set of book")
    public void calculatePriceForDistinctBookSet_ShouldCalculatePriceAndDiscount() {

        List<BookRequest> bookRequests = Arrays.asList(new BookRequest(1, 1),new BookRequest(2, 1));

        BookResponse price = calculateBookPriceService.calculatePrice(bookRequests);
        BookGroup group = price.getListOfBookGroups().get(0);

        assertEquals(2, group.getNumberOfBooks ());
        assertEquals(100.0, group.getActualPrice());
        assertEquals(5.0, group.getDiscountAmount());
        assertEquals(5.0, group.getDiscountPercentage());
        assertEquals(95.0, group.getFinalGroupPrice());
    }
    @Test
    @DisplayName ("Invalid Book ID should throw exception")
    public void calculatePrice_withInvalidBookId_shouldThrowException() {

        List<BookRequest> invalidBookId = Arrays.asList(new BookRequest(99, 1));

        assertThrows(InvalidBookException.class, () -> calculateBookPriceService.calculatePrice (invalidBookId));
    }
}
