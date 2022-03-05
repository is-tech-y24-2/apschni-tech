package ru.ifmo.banks.banks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankInterestRate {
    private Integer limit;
    private Double percent;
}
