package ru.ifmo.banks.accounts;

import lombok.NoArgsConstructor;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;
import ru.ifmo.banks.transactions.Transaction;

import java.time.Instant;

@NoArgsConstructor
public class DebitAccount extends Account implements Cloneable{

    public DebitAccount(Client client, Integer unverifiedLimit) {
        super(client, unverifiedLimit);
    }

    @Override
    public void transfer(Double transactionSum, Account destinationAccount, Instant dateTime) throws BankException {
        if (balance < transactionSum) {
            throw new BankException("Not enough money");
        }
        Transaction transaction = new Transaction(Transaction.TransactionType.Transfer, dateTime, this);
        transaction.commit(transactionSum, destinationAccount);
        transactions.add(transaction);
    }

    @Override
    public void withdrawal(Double transactionSum, Instant dateTime) throws BankException {
        if (balance < transactionSum) {
            throw new BankException("Not enough money");
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

    public void addInterest(Double interestSum, Instant dateTime) throws BankException {
        Transaction transaction = new Transaction(Transaction.TransactionType.AddInterest, dateTime, this);
        transaction.commit(interestSum, null);
        transactions.add(transaction);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
