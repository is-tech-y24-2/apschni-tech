package ru.ifmo.banks.accounts;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;
import ru.ifmo.banks.transactions.Transaction;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CreditAccount extends Account implements Cloneable{
    private Integer belowZeroLimit;

    public CreditAccount(Client client, Integer unverifiedLimit, Integer belowZeroLimit) {
        super(client, unverifiedLimit);
        this.belowZeroLimit = belowZeroLimit;
    }

    @Override
    public void transfer(Double transactionSum, Account destinationAccount, Instant dateTime) throws BankException {
        if (balance - transactionSum + belowZeroLimit < 0) {
            throw new BankException("Credit Limit reached");
        }

        if (balance < transactionSum){
            throw new BankException("Not enough money");
        }

        Transaction transaction = new Transaction(Transaction.TransactionType.Transfer, dateTime, this);
        transaction.commit(transactionSum, destinationAccount);
        transactions.add(transaction);
    }

    @Override
    public void withdrawal(Double transactionSum, Instant dateTime) throws BankException {
        if (balance - transactionSum + belowZeroLimit < 0) {
            throw new BankException("Credit Limit reached");
        }

        Transaction transaction = new Transaction(Transaction.TransactionType.Withdrawal, dateTime, this);
        transaction.commit(transactionSum, null);
        transactions.add(transaction);
    }

    @Override
    public void refill(Double transactionSum, Instant dateTime) throws BankException {
        Transaction transaction = new Transaction(Transaction.TransactionType.Refill, dateTime, this);
        transaction.commit(transactionSum, null);
        transactions.add(transaction);
    }

    public void subtractCommission(Double commissionSum, Instant dateTime) throws BankException {
        Transaction transaction = new Transaction(Transaction.TransactionType.SubtractCommission, dateTime, this);
        transaction.commit(commissionSum, null);
        transactions.add(transaction);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
