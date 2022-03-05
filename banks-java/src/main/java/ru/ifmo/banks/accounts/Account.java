package ru.ifmo.banks.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;
import ru.ifmo.banks.transactions.Transaction;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account {

    protected Boolean clientSubscribed = false;
    protected Client client;
    protected Double balance = 0d;
    protected UUID id = UUID.randomUUID();
    protected Instant lastDayInterestsCommissionPaid = Instant.now();
    protected Double tempInterestCommissionSum = 0d;
    protected Integer unverifiedLimit = 0;
    protected List<Transaction> transactions = new ArrayList<>();

    public Account(Client client, Integer unverifiedLimit) {
        this.client = client;
        this.unverifiedLimit = unverifiedLimit;
    }

    public abstract void transfer(Double transactionSum, Account destinationAccount, Instant dateTime) throws BankException;

    public abstract void withdrawal(Double transactionSum, Instant dateTime) throws BankException;

    public abstract void refill(Double transactionSum, Instant dateTime) throws BankException;

    public static AccountType getAccountType(Account account) {
        if (account.getClass().equals(CreditAccount.class)) {
            return AccountType.Credit;
        } else if (account.getClass().equals(DepositAccount.class)) {
            return AccountType.Deposit;
        } else if (account.getClass().equals(DebitAccount.class)) {
            return AccountType.Debit;
        } else return AccountType.Any;
    }

    public void undoTransaction(UUID transactionId) throws BankException {
        Optional<Transaction> optionalTransaction = transactions.stream()
                .filter(transaction -> transaction.getId().equals(transactionId))
                .findAny();
        if (!optionalTransaction.isPresent()) {
            throw new BankException("No transaction found");
        }
        Transaction transaction = optionalTransaction.get();
        transaction.rollback();
        transactions.remove(transaction);
    }
}
