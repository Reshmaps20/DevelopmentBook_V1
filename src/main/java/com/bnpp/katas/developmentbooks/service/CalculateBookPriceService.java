package com.bnpp.katas.developmentbooks.service;

public class CalculateBookPriceService {

    public static final double PRICE = 50.0;

    public double calculatePrice(int bookId, int quantity) {
        return quantity * PRICE;
    }
}
