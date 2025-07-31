package com.bnpp.katas.developmentbooks.validator;

import com.bnpp.katas.developmentbooks.exception.InvalidBookException;
import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.store.BooksEnum;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookValidator {

    public void validateBooks (List<BookRequest> bookRequest) {

        Set<Integer> validBookIds = Arrays.stream(BooksEnum.values())
                .map(BooksEnum::getId)
                .collect(Collectors.toSet());

        List<Integer> invalidBookIds = bookRequest.stream ()
                .map (BookRequest::getId)
                .filter (id -> !validBookIds.contains (id))
                .collect (Collectors.toList ());

        if (!invalidBookIds.isEmpty ()) {
            throw new InvalidBookException ("Invalid book IDs : " + invalidBookIds);
        }
    }
}
