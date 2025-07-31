package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import com.bnpp.katas.developmentbooks.store.DiscountEnum;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalculateBookPriceService {

    public double calculatePrice(List<BookRequest> bookRequest) {

        Map<BooksEnum, Integer> bookCountMap = getBookCounts(bookRequest);
        List<Double> possiblePrices = new ArrayList<>();

        List<Integer> applicableDiscountSet = getApplicableDiscounts(bookCountMap.size());

        applicableDiscountSet.forEach(discountSize -> {
            double totalPriceForBooks = calculateCombinationPrice(discountSize,bookCountMap);
            possiblePrices.add(totalPriceForBooks);
        });

        return possiblePrices.stream().min(Double::compare).orElse(0.0);
    }

    private List<Integer> getApplicableDiscounts(int numberOfBooks) {

        List<Integer> applicableDiscounts = Arrays.stream(DiscountEnum.values())
                .filter(level -> level.getNumberOfDistinctItems() <= numberOfBooks)
                .map(DiscountEnum::getNumberOfDistinctItems).sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return applicableDiscounts.isEmpty() ? Collections.singletonList(1) : applicableDiscounts;

    }

    private double calculateCombinationPrice(int bookGroupSize, Map<BooksEnum, Integer> bookCountMap) {
        Map<BooksEnum, Integer> booksCount = new HashMap<> (bookCountMap);
        double totalPrice = 0.0;

        while (hasBooksLeft(booksCount)) {

            List<BooksEnum> selectedBooks = selectBooks(booksCount,bookGroupSize);
            if (!selectedBooks.isEmpty()) {
                double discount = getDiscount(selectedBooks.size());
                double actualPrice = selectedBooks.size() * 50;
                totalPrice += actualPrice * (1 - discount);
            }
        }
        return totalPrice;
    }

    private List<BooksEnum> selectBooks(Map<BooksEnum, Integer> booksCount, int numberOfBooks) {
        List<BooksEnum> selectedBooks = new ArrayList<>();

        Arrays.stream(BooksEnum.values()).filter(bookEnum -> booksCount.getOrDefault(bookEnum, 0) > 0)
                .forEach(bookEnum -> {
                    if (selectedBooks.size() < numberOfBooks) {
                        selectedBooks.add(bookEnum);
                        booksCount.put(bookEnum, booksCount.get(bookEnum) - 1);
                    }
                });

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
