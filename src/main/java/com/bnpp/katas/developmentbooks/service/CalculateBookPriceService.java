package com.bnpp.katas.developmentbooks.service;

import com.bnpp.katas.developmentbooks.model.BookGroup;
import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.model.BookResponse;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import com.bnpp.katas.developmentbooks.store.DiscountEnum;
import com.bnpp.katas.developmentbooks.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.bnpp.katas.developmentbooks.constants.Constants.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculateBookPriceService {

    private final BookValidator bookValidator;

    public BookResponse calculatePrice(List<BookRequest> bookRequest) {

        bookValidator.validateBooks(bookRequest);

        Map<BooksEnum, Integer> bookCountMap = getBookCounts(bookRequest);
        List<Integer> applicableDiscountSet = getApplicableDiscountSet(bookCountMap.size());
        Map<Double, List<BookGroup>> priceToGroupMap = calculateTotalPrice (applicableDiscountSet,bookCountMap);

        return findMinimumPriceResponse(priceToGroupMap);
    }

    private Map<Double, List<BookGroup>> calculateTotalPrice (List<Integer> applicableDiscountSet,Map<BooksEnum, Integer> bookCountMap) {

        Map<Double, List<BookGroup>> totalBookPrices = applicableDiscountSet.stream()
                .flatMap(discountSize -> calculateCombinationPrice(discountSize,bookCountMap)
                        .entrySet().stream()).collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing
                ));

        return totalBookPrices;
    }

    private BookResponse findMinimumPriceResponse(Map<Double, List<BookGroup>> totalPrices) {

        Optional<Double> minPrice = totalPrices.keySet().stream().reduce(Double::min);
        List<BookGroup> associatedBookGroups = totalPrices.get(minPrice.get());

        return createBookResponse(associatedBookGroups, minPrice.get());
    }

    private BookResponse createBookResponse(List<BookGroup> bookGroups, double finalPrice) {

        double actualPrice = calculateActualPrice(bookGroups);
        double totalDiscount = calculateTotalDiscount(bookGroups);

        return BookResponse.builder()
                .listOfBookGroups(bookGroups)
                .actualPrice(actualPrice)
                .totalDiscount(totalDiscount)
                .finalPrice(finalPrice)
                .build();
    }

    private double calculateTotalDiscount(List<BookGroup> bookGroups) {
        return bookGroups.stream().mapToDouble(BookGroup::getDiscountAmount).sum();
    }

    private double calculateActualPrice(List<BookGroup> bookGroups) {
        return bookGroups.stream().mapToDouble(BookGroup::getActualPrice).sum();
    }

    private List<Integer> getApplicableDiscountSet(int numberOfBooks) {

        List<Integer> applicableDiscounts = Arrays.stream(DiscountEnum.values())
                .filter(level -> level.getNumberOfDistinctItems() <= numberOfBooks)
                .map(DiscountEnum::getNumberOfDistinctItems).sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return applicableDiscounts.isEmpty() ? Collections.singletonList(ONE) : applicableDiscounts;
    }

    private Map<Double, List<BookGroup>> calculateCombinationPrice(int bookGroupSize,Map<BooksEnum, Integer> OriginalBooksCount) {

        Map<BooksEnum, Integer> booksCount = new HashMap<>(OriginalBooksCount);
        Map<Double, List<BookGroup>> priceToGroupMap = new HashMap<>();
        List<BookGroup> bookGroups = new ArrayList<>();
        double[] totalPrice =new double[ONE];
        processBooks(booksCount, bookGroupSize, bookGroups, totalPrice);
        priceToGroupMap.put(totalPrice[ZERO], bookGroups);
        return priceToGroupMap;
    }

    private void processBooks(Map<BooksEnum, Integer> booksCount, int bookGroupSize, List<BookGroup> bookGroups, double[] totalPrice) {

        if (!hasBooksLeft(booksCount)) {
            return;
        }

        List<BooksEnum> selectedBooks = selectBooks(booksCount, bookGroupSize);
        if (!selectedBooks.isEmpty()) {

            double discount = getDiscount(selectedBooks.size());
            double actualPrice = selectedBooks.size() * DiscountEnum.PRICE;
            totalPrice[ZERO] += actualPrice * (ONE - discount);
            bookGroups.add(createBookGroup(selectedBooks, discount, actualPrice));

            processBooks(booksCount, bookGroupSize, bookGroups, totalPrice);
        }
    }

    private BookGroup createBookGroup(List<BooksEnum> selectedBooks, double discount, double actualPrice) {

        return BookGroup.builder()
                .bookIds (selectedBooks.stream().map(BooksEnum::getId).collect(Collectors.toList()))
                .numberOfBooks(selectedBooks.size())
                .discountPercentage(discount * HUNDRED)
                .actualPrice(actualPrice)
                .discountAmount(actualPrice * discount)
                .finalGroupPrice(actualPrice - (actualPrice * discount))
                .build();
    }

    private List<BooksEnum> selectBooks(Map<BooksEnum, Integer> booksCount, int bookGroupSize) {

        List<BooksEnum> selectedBooks = new ArrayList<>();

        Arrays.stream(BooksEnum.values()).filter(bookEnum -> booksCount.getOrDefault(bookEnum, ZERO) > ZERO)
                .forEach(bookEnum -> {
                    if (selectedBooks.size() < bookGroupSize) {
                        selectedBooks.add(bookEnum);
                        booksCount.put(bookEnum, booksCount.get(bookEnum) - ONE);
                    }
                });

        return selectedBooks;
    }

    private boolean hasBooksLeft(Map<BooksEnum, Integer> bookCountsCopy) {
        return bookCountsCopy.values().stream().anyMatch(count -> count > ZERO);
    }

    private Map<BooksEnum, Integer> getBookCounts(List<BookRequest> bookRequest) {

        Map<BooksEnum, Integer> bookCounts = new LinkedHashMap<>();

        bookRequest.forEach(request -> {
            BooksEnum book = BooksEnum.values()[request.getId() - ONE];
            bookCounts.put(book, request.getQuantity());
        });

        return bookCounts;
    }

    public static double getDiscount(int uniqueBookCount) {
        return Arrays.stream(DiscountEnum.values())
                .filter(discount -> discount.getNumberOfDistinctItems() == uniqueBookCount)
                .map(DiscountEnum::getDiscountPercentage).findFirst().orElse(ZERO_DISCOUNT);
    }
}
