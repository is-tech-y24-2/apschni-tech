package ru.ifmo.banks.transactions;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.banks.accounts.Account;
import ru.ifmo.banks.exceptions.BankException;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    public enum TransactionType {
        Transfer,
        Withdrawal,
        Refill,
        SubtractCommission,
        AddInterest,
    }

    private Double balanceBefore;
    private Double balanceAfter;
    private UUID id;
    private TransactionType transactionType;
    private Double transactionSum;
    private Instant date;
    private Account sourceAccount;
    private Account destAccount;
    private Boolean cancelled = false;

    public Transaction(TransactionType transactionType, Instant date, Account sourceAccount) {
        this.transactionType = transactionType;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.id = UUID.randomUUID();
        this.balanceBefore = sourceAccount.getBalance();
    }

    public void commit(Double transactionSum, Account destAccount) throws BankException {
        if (cancelled) {
            throw new BankException("Operation is already canceled");
        }

        if (transactionSum < 0) {
            throw new BankException("Operation Sum less than zero");
        }

        this.transactionSum = transactionSum;
        this.destAccount = destAccount;

        switch (transactionType) {
            case SubtractCommission:
                sourceAccount.setBalance(sourceAccount.getBalance() - transactionSum);
                balanceAfter = sourceAccount.getBalance();
                break;
            case Withdrawal:
                if (!sourceAccount.getClient().isVerified() && transactionSum > sourceAccount.getUnverifiedLimit()) {
                    throw new BankException("Unverified account cant withdraw above limit");
                }
                sourceAccount.setBalance(sourceAccount.getBalance() - transactionSum);
                balanceAfter = sourceAccount.getBalance();
                break;
            case AddInterest:
            case Refill:
                sourceAccount.setBalance(sourceAccount.getBalance() + transactionSum);
                balanceAfter = sourceAccount.getBalance();
                break;
            case Transfer:
                if (destAccount == null) {
                    throw new BankException("Wrong Transfer destination");
                }
                if (!sourceAccount.getClient().isVerified() && transactionSum > sourceAccount.getUnverifiedLimit()) {
                    throw new BankException("Unverified account cant transfer above limit");
                }
                sourceAccount.setBalance(sourceAccount.getBalance() - transactionSum);
                destAccount.setBalance(destAccount.getBalance() + transactionSum);
                balanceAfter = sourceAccount.getBalance();
                break;
        }
    }

    public void rollback() throws BankException {
        if (cancelled) {
            throw new BankException("Operation is already canceled");
        }

        cancelled = true;
        balanceAfter = balanceBefore;

        switch (transactionType) {
            case Transfer:
                destAccount.setBalance(destAccount.getBalance() + transactionSum);
                destAccount.setBalance(destAccount.getBalance() - transactionSum);
                break;
            case Refill:
            case AddInterest:
                sourceAccount.setBalance(sourceAccount.getBalance() - transactionSum);
                break;
            case Withdrawal:
            case SubtractCommission:
                sourceAccount.setBalance(sourceAccount.getBalance() + transactionSum);
                break;
        }
    }
}
