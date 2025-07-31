package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import com.bnpp.katas.developmentbooks.store.DiscountEnum;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculateBookPriceService {

    public double calculatePrice(List<BookRequest> bookRequest) {

        double totalPrice = 0.0;
        Map<BooksEnum, Integer> bookCountMap = getBookCounts(bookRequest);

        Map<BooksEnum, Integer> booksCount = new HashMap<> (bookCountMap);
        while (hasBooksLeft(booksCount)) {

            List<BooksEnum> selectedBooks = selectBooks(booksCount);
            if (!selectedBooks.isEmpty()) {
                double discount = getDiscount(selectedBooks.size());
                double actualPrice = selectedBooks.size() * 50;
                totalPrice += actualPrice * (1 - discount);
            }
        }
        return totalPrice;
    }

    private List<BooksEnum> selectBooks(Map<BooksEnum, Integer> booksCount) {
        List<BooksEnum> selectedBooks = new ArrayList<>();
        for (BooksEnum bookEnum : BooksEnum.values()) {
            if (booksCount.getOrDefault(bookEnum, 0) > 0) {
                selectedBooks.add(bookEnum);
                booksCount.put(bookEnum, booksCount.get(bookEnum) - 1);
            }
        }
        return selectedBooks;
    }


    private boolean hasBooksLeft(Map<BooksEnum, Integer> booksCount) {
        return booksCount.values().stream().anyMatch(count -> count > 0);
    }

    private Map<BooksEnum, Integer> getBookCounts(List<BookRequest> bookRequest) {

        Map<BooksEnum, Integer> bookCounts = new LinkedHashMap<> ();
        bookRequest.forEach(request -> {
            BooksEnum book = BooksEnum.values()[request.getId() - 1];
            bookCounts.put(book, request.getQuantity());
        });

        return bookCounts;
    }

    private double getDiscount(int uniqueBookCount) {
        return Arrays.stream(DiscountEnum.values())
                .filter(discount -> discount.getNumberOfDistinctItems() == uniqueBookCount)
                .map(DiscountEnum::getDiscountPercentage).findFirst().orElse(0.0);
    }
}
