package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.store.BooksEnum;
import java.util.Arrays;

public class CalculateBookPriceService {

    public double calculatePrice(int bookId, int quantity) {

        BooksEnum book = Arrays.stream(BooksEnum.values())
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElse(null);

        return book.getPrice() * quantity;
    }
}
