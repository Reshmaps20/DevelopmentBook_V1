package com.bnpp.katas.developmentbooks.validator;

import com.bnpp.katas.developmentbooks.exception.InvalidBookException;
import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.bnpp.katas.developmentbooks.constants.Constants.*;

@Component
public class BookValidator {

    public void validateBooks (List<BookRequest> bookRequest) {

        checkForValidBooks(bookRequest);
        checkForValidQuantities(bookRequest);
    }

    private void checkForValidQuantities (List<BookRequest> bookRequest) {

        List<Integer> invalidQuantities = getInvalidBookQuantities (bookRequest);

        if (!invalidQuantities.isEmpty()) {
            throw new InvalidBookException(INVALID_BOOK_QUANTITY + invalidQuantities);
        }
    }

    private static List<Integer> getInvalidBookQuantities (List<BookRequest> bookRequest) {

        return bookRequest.stream().map(BookRequest::getQuantity).filter(qty -> qty <= ZERO)
                .collect(Collectors.toList());
    }

    private void checkForValidBooks (List<BookRequest> bookRequest) {

        Set<Integer> validBookIds = getValidBooks ();
        List<Integer> invalidBookIds = checkInvalidBookIds(bookRequest, validBookIds);

        if (!invalidBookIds.isEmpty()) {
            throw new InvalidBookException (INVALID_BOOK_ID + invalidBookIds);
        }
    }

    private static List<Integer> checkInvalidBookIds (List<BookRequest> bookRequest, Set<Integer> validBookIds) {

        return bookRequest.stream().map(BookRequest::getId).filter(id -> !validBookIds.contains(id))
                .collect(Collectors.toList());
    }

    private static Set<Integer> getValidBooks () {

        return Arrays.stream(BooksEnum.values()).map(BooksEnum::getId)
                .collect(Collectors.toSet());
    }
}
