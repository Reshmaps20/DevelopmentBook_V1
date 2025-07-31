package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CalculateBookPriceService {

    public double calculatePrice(List<BookRequest> bookRequest) {

        double discount = 0.0;
        double totalPrice = bookRequest.stream().mapToDouble(bookReq -> {
            BooksEnum book = Arrays.stream(BooksEnum.values())
                    .filter(b -> b.getId() == bookReq.getId())
                    .findFirst()
                    .orElse(null);
            return (book != null) ? book.getPrice() * bookReq.getQuantity() : 0.0;
        }).sum();

        long uniqueBookCount = bookRequest.stream().map(BookRequest::getId).distinct().count();

        if (uniqueBookCount == 3) {
            discount = 0.10;
        }else if (uniqueBookCount == 2) {
            discount = 0.05;
        }

        return totalPrice * (1 - discount);
    }
}
