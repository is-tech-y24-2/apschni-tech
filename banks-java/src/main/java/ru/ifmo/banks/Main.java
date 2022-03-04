package ru.ifmo.banks;

import ru.ifmo.banks.accounts.Account;
import ru.ifmo.banks.accounts.AccountType;
import ru.ifmo.banks.banks.Bank;
import ru.ifmo.banks.banks.BankInterestRate;
import ru.ifmo.banks.banks.CentralBank;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws BankException {
        Scanner scanner = new Scanner(System.in);
        List<BankInterestRate> bankInterestRates = Arrays.asList(
                new BankInterestRate(5000, 2d),
                new BankInterestRate(10000, 3.5)
        );
        CentralBank centralBank = new CentralBank();
        Bank bank = centralBank.registerBank("Tinkoff Bank", 4d, 10000, 15000, 1000d, bankInterestRates);
        Client client = Client.builder()
                .name("Asd")
                .surname("Zxc")
                .address("Aboba 5")
                .build();

        while (true) {
            List<Account> accounts = bank.getAccounts();
            System.out.println("\nPick an option:\n" +
                    "1. Create new Account\n" +
                    "2.Transfer money\n" +
                    "3. Withdraw money\n" +
                    "4. Refill Account\n" +
                    "5. Check Balance\n" +
                    "6. Exit\n");

            switch (scanner.nextLine()){
                case "1":
                    System.out.println("What Account do you want to create?\n" +
                            "1. Debit Account\n" +
                            "2. Deposit Account\n" +
                            "3. Credit Account\n");
                    switch (scanner.nextLine()){
                        case "1" -> bank.addNewAccount(client, AccountType.Debit, null);
                        case "2" -> bank.addNewAccount(client, AccountType.Deposit, Instant.now().plus(356, ChronoUnit.DAYS));
                        case "3" -> bank.addNewAccount(client, AccountType.Credit, null);

                    }
                    System.out.println("Account created");
                    break;
                case "2" :
                    System.out.println("Choose account number to transfer from and account to transfer to:\\n");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println("Account " + (i+1) + "\n");
                    }
                    int accountNumber1 = Integer.parseInt(scanner.nextLine());
                    int accountNumber2 = Integer.parseInt(scanner.nextLine());
                    System.out.println("How much u want to transfer? U have " + accounts.get(accountNumber1 - 1).getBalance()+"\n");
                    accounts.get(accountNumber1 - 1)
                            .transfer(Double.parseDouble(scanner.nextLine()), accounts.get(accountNumber2 - 1), Instant.now());
                    System.out.println("Transfer done");
                    break;
                case "3":
                    System.out.println("Choose account number to withdraw from\n");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println("Account " + (i+1) + "\n");
                    }
                    int accountNumber = Integer.parseInt(scanner.nextLine());
                    System.out.println("How much u want to withdraw? U have " + accounts.get(accountNumber - 1).getBalance()+"\n");
                    accounts.get(accountNumber - 1)
                            .withdrawal(Double.parseDouble(scanner.nextLine()), Instant.now());
                    System.out.println("Withdraw done");
                    break;
                case "4":
                    System.out.println("Choose account number to refill\n");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println("Account " + (i+1) + "\n");
                    }
                    int accountNumber3 = Integer.parseInt(scanner.nextLine());
                    System.out.println("How much u want to refill? U have " + accounts.get(accountNumber3 - 1).getBalance()+"\n");
                    accounts.get(accountNumber3 - 1)
                            .refill(Double.parseDouble(scanner.nextLine()), Instant.now());
                    System.out.println("Refill done");
                    break;
                case "5":
                    System.out.println("Choose account number to check balance\n");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println("Account " + (i+1) + "\n");
                    }
                    int accountNumber4 = Integer.parseInt(scanner.nextLine());
                    System.out.println("You have " + accounts.get(accountNumber4 - 1).getBalance()+"\n");
                    break;
                case "6":
                    return;
            }
        }
    }
}
