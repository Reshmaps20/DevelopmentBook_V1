package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculateBookPriceService {

    public double calculatePrice(List<BookRequest> bookRequest) {

        double totalPrice = 0.0;
        Map<BooksEnum, Integer> bookCountMap = getBookCounts(bookRequest);

        Map<BooksEnum, Integer> bookCountsCopy = new HashMap<> (bookCountMap);
        while ((bookCountsCopy.values().stream().anyMatch(count -> count > 0))) {

            List<BooksEnum> selectedBooks = new ArrayList<>();
            for (BooksEnum bookEnum : BooksEnum.values()) {
                if (bookCountsCopy.getOrDefault(bookEnum, 0) > 0) {
                    selectedBooks.add(bookEnum);
                    bookCountsCopy.put(bookEnum, bookCountsCopy.get(bookEnum) - 1);
                }
            }
            if (!selectedBooks.isEmpty()) {
                double discount = getDiscount(selectedBooks.size());
                double actualPrice = selectedBooks.size() * 50;
                totalPrice += actualPrice * (1 - discount);
            }
        }
        return totalPrice;
    }

    private Map<BooksEnum, Integer> getBookCounts(List<BookRequest> bookRequest) {

        Map<BooksEnum, Integer> bookCounts = new LinkedHashMap<> ();
        bookRequest.forEach(request -> {
            BooksEnum book = BooksEnum.values()[request.getId() - 1];
            bookCounts.put(book, request.getQuantity());
        });

        return bookCounts;
    }

    private double getDiscount(int bookCount) {
        if (bookCount == 3) {
            return 0.10;
        } else if (bookCount == 2) {
            return 0.05;
        } else {
            return 0.0;
        }
    }
}
