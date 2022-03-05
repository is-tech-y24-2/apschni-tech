package ru.ifmo.banks.banks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ifmo.banks.accounts.*;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;
import ru.ifmo.banks.transactions.Transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Bank {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final List<Account> accounts = new ArrayList<>();
    private Double debitPercent;
    private Integer unverifiedLimit;
    private Integer creditBelowZeroLimit;
    private Double creditCommission;
    private List<BankInterestRate> interestRates = new ArrayList<>();

    public Double checkDepositAccountBalanceAfterTime(DepositAccount account, Instant skipTime) throws CloneNotSupportedException {
        DepositAccount depositAccountCopy = (DepositAccount) account.clone();
        depositAccountCopy.setTempInterestCommissionSum(0.0);
        calculatePercentsForAccount(depositAccountCopy, skipTime, calculateDepositPercent(depositAccountCopy.getDepositSum()));
        return depositAccountCopy.getBalance() + depositAccountCopy.getTempInterestCommissionSum();
    }

    public Double checkDebitAccountBalanceAfterTime(DebitAccount account, Instant skipTime) throws CloneNotSupportedException {
        DebitAccount debitAccountCopy = (DebitAccount) account.clone();
        debitAccountCopy.setTempInterestCommissionSum(0.0);
        calculatePercentsForAccount(debitAccountCopy, skipTime, debitPercent);
        return debitAccountCopy.getBalance() + debitAccountCopy.getTempInterestCommissionSum();
    }

    public Double checkCreditAccountBalanceAfterTime(CreditAccount account, Instant skipTime) throws CloneNotSupportedException {
        CreditAccount creditAccountCopy = (CreditAccount) account.clone();
        creditAccountCopy.setTempInterestCommissionSum(0.0);
        calculateCreditCommissionsForAccount(creditAccountCopy, skipTime);
        return creditAccountCopy.getBalance() - creditAccountCopy.getTempInterestCommissionSum();
    }

    public void payInterestsAndCommissions(Instant dateTime) throws BankException {
        for (Account account : accounts) {
            if (DebitAccount.class.equals(account.getClass())) {
                DebitAccount debitAccount = (DebitAccount) account;
                calculatePercentsForAccount(debitAccount, dateTime, debitPercent);
                debitAccount.addInterest(debitAccount.getTempInterestCommissionSum(), dateTime);
                debitAccount.setTempInterestCommissionSum(0.0);
            } else if (DepositAccount.class.equals(account.getClass())) {
                DepositAccount depositAccount = (DepositAccount) account;
                calculatePercentsForAccount(depositAccount, dateTime, calculateDepositPercent(depositAccount.getDepositSum()));
                depositAccount.addInterest(depositAccount.getTempInterestCommissionSum(), dateTime);
                depositAccount.setTempInterestCommissionSum(0.0);
            } else if (CreditAccount.class.equals(account.getClass())) {
                CreditAccount creditAccount = (CreditAccount) account;
                calculateCreditCommissionsForAccount(creditAccount, dateTime);
                creditAccount.subtractCommission(creditAccount.getTempInterestCommissionSum(), dateTime);
                creditAccount.setTempInterestCommissionSum(0.0);
            }
        }
    }

    public Account addNewAccount(Client client, AccountType accountType, Instant depositExpirationDay) {
        switch (accountType) {
            case Debit:
                DebitAccount debitAccount = new DebitAccount(client, unverifiedLimit);
                accounts.add(debitAccount);
                return debitAccount;
            case Credit:
                CreditAccount creditAccount = new CreditAccount(client, unverifiedLimit, creditBelowZeroLimit);
                accounts.add(creditAccount);
                return creditAccount;
            case Deposit:
                DepositAccount depositAccount = new DepositAccount(client, unverifiedLimit, depositExpirationDay);
                accounts.add(depositAccount);
                return depositAccount;
        }

        return null;
    }

    public void notifySubscribers(AccountType accountType, String message) {
        accounts.stream()
                .filter(Account::getClientSubscribed)
                .filter(account -> (Account.getAccountType(account).equals(accountType)) || accountType.equals(AccountType.Any))
                .map(Account::getClient)
                .forEach(client -> client.getUpdate(message));
    }

    public void subscribeClient(Account account) {
        account.setClientSubscribed(true);
    }

    public void changeDebitPercent(Double newPercent) {
        debitPercent = newPercent;
        notifySubscribers(AccountType.Debit, "Debit percentage changed");
    }

    public void changeCreditCommission(Double newCommission) {
        creditCommission = newCommission;
        notifySubscribers(AccountType.Credit, "Credit commission changed");
    }

    public void changeUnverifiedLimit(Integer newLimit) {
        unverifiedLimit = newLimit;
        notifySubscribers(AccountType.Any, "Unverified limit changed");
    }

    public void changeCreditBelowZeroLimit(Integer newLimit) {
        creditBelowZeroLimit = newLimit;
        notifySubscribers(AccountType.Any, "BelowZero limit changed");
    }

    public void changeInterestRates(List<BankInterestRate> newInterestRates) {
        interestRates = newInterestRates;
        notifySubscribers(AccountType.Deposit, "Interest rates changed");
    }

    public void calculatePercentsForAccount(Account account, Instant dateTime, Double percents) {
        if (account instanceof CreditAccount) return;
        if (account.getLastDayInterestsCommissionPaid() == null) {
            account.setLastDayInterestsCommissionPaid(account.getTransactions().get(0).getDate());
        }

        for (Instant curDate = account.getLastDayInterestsCommissionPaid();
             curDate.isBefore(dateTime);
             curDate = curDate.plus(1, ChronoUnit.DAYS)) {
            Transaction lastTransaction =
                    account.getTransactions()
                            .stream()
                            .max(Comparator.comparing(Transaction::getDate))
                            .orElse(null);
            if (lastTransaction == null) continue;
            Double curBalance = lastTransaction.getBalanceAfter();
            account.setTempInterestCommissionSum(account.getTempInterestCommissionSum() + (curBalance * (percents / 36500)) +
                    account.getTempInterestCommissionSum() * (percents / 36500));
        }

        account.setLastDayInterestsCommissionPaid(dateTime);
    }

    public void calculateCreditCommissionsForAccount(CreditAccount account, Instant dateTime) {
        if (account.getLastDayInterestsCommissionPaid() == null) {
            account.setLastDayInterestsCommissionPaid(account.getTransactions().get(0).getDate());
        }

        for (Instant curDate = account.getLastDayInterestsCommissionPaid();
             curDate.isBefore(dateTime);
             curDate = curDate.plus(30, ChronoUnit.DAYS)) {
            Transaction lastTransaction =
                    account.getTransactions()
                            .stream()
                            .max(Comparator.comparing(Transaction::getDate))
                            .orElse(null);
            if (lastTransaction == null) continue;
            if (lastTransaction.getBalanceAfter() < 0) {
                account.setTempInterestCommissionSum(account.getTempInterestCommissionSum() + creditCommission);
            }
        }

        account.setLastDayInterestsCommissionPaid(dateTime);
    }

    private Double calculateDepositPercent(Double depositSum) {
        for (BankInterestRate interestRate : interestRates) {
            if (depositSum < interestRate.getLimit()) return interestRate.getPercent();
        }

        return interestRates.get(interestRates.size() - 1).getPercent();
    }
}
