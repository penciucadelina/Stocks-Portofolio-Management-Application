/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.services;

import lombok.Data;

import java.math.BigDecimal;


@Data // at compile time, it creates getters, setters, equals(), hashCode() and toString()
public class StockItem {
    private String symbol;
    private BigDecimal price;
    private BigDecimal change;
    private String currency;
    private String exchange;
    private String name;
}
