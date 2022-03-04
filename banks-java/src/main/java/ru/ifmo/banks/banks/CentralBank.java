package ru.ifmo.banks.banks;

import lombok.NoArgsConstructor;
import ru.ifmo.banks.exceptions.BankException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CentralBank {
    private final List<Bank> banks = new ArrayList<>();

    public void notify(Instant dateTime) throws BankException {
        for(Bank bank : banks)
        {
            bank.payInterestsAndCommissions(dateTime);
        }
    }

    public Bank registerBank(String name, Double debitPercent, Integer unverifiedLimit, Integer creditBelowZeroLimit, Double creditCommission, List<BankInterestRate> interestRates)
    {
        Bank bank = new Bank(name, debitPercent, unverifiedLimit, creditBelowZeroLimit, creditCommission, interestRates);
        banks.add(bank);
        return bank;
    }
}
