package com.bnpp.katas.developmentbooks.controller;

import com.bnpp.katas.developmentbooks.exception.InvalidBookException;
import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.model.BookResponse;
import com.bnpp.katas.developmentbooks.service.CalculateBookPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bnpp.katas.developmentbooks.constants.Constants.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${bookstore.controller.path}")
public class BookStoreController {

    private final CalculateBookPriceService calculateBookPriceService;

    @PostMapping("${bookstore.endpoint.calculatePrice}")
    public ResponseEntity<BookResponse> calculatePrice(@RequestBody List<BookRequest> request) {
        if (!CollectionUtils.isEmpty(request)) {
            return new ResponseEntity<> (calculateBookPriceService.calculatePrice (request), HttpStatus.OK);
        }
        throw new InvalidBookException (INVALID_BOOK_REQUEST);
    }
}
