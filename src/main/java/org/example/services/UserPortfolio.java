
package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;


@Data // it creates getters, setters, equals(), hashCode() and toString() (at compile time)
@AllArgsConstructor // it creates the constructor with arguments for all the attributes (at compile time)
public class UserPortfolio {
    private BigDecimal cash;

    private Map<String, Integer> shares; // a map of number of shares by stock symbol
}
