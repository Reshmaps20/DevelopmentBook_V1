package com.bnpp.katas.developmentbooks.controller;

import com.bnpp.katas.developmentbooks.model.BookRequest;
import com.bnpp.katas.developmentbooks.model.BookResponse;
import com.bnpp.katas.developmentbooks.service.CalculateBookPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class BookStoreController {

    private final CalculateBookPriceService calculateBookPriceService;

    @PostMapping("/calculateprice")
    public ResponseEntity<BookResponse> calculatePrice(@RequestBody List<BookRequest> request) {
        return new ResponseEntity<> (calculateBookPriceService.calculatePrice (request), HttpStatus.OK);
    }
}
