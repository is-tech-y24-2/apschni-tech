import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ifmo.banks.accounts.Account;
import ru.ifmo.banks.accounts.AccountType;
import ru.ifmo.banks.banks.Bank;
import ru.ifmo.banks.banks.BankInterestRate;
import ru.ifmo.banks.banks.CentralBank;
import ru.ifmo.banks.clients.Client;
import ru.ifmo.banks.exceptions.BankException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class BanksTest {
    private Bank bank;
    private Client client1;
    private Client client2;
    private Account debitAccount1;
    private Account debitAccount2;
    private CentralBank centralBank;

    @BeforeEach
    public void setup() throws BankException {
        List<BankInterestRate> interestRates = Arrays.asList(
                new BankInterestRate(5000, 2d),
                new BankInterestRate(10000, 3.5));
        centralBank = new CentralBank();
        bank = centralBank.registerBank("Tinkoff Bank", 4d, 10000, 15000, 1000d, interestRates);
        client1 = Client.builder()
                .name("Artem")
                .surname("Pasichnik")
                .address("Vyazemskiy 5-7")
                .passport("1111111111")
                .build();
        client2 = Client.builder()
                .name("Anton")
                .surname("Zhmyx")
                .address("Vyazemskiy 5-7")
                .build();
        debitAccount1 = bank.addNewAccount(client1, AccountType.Debit, null);
        debitAccount2 = bank.addNewAccount(client2, AccountType.Debit, null);
        debitAccount1.refill(5000d, Instant.now());
        debitAccount2.refill(1000d, Instant.now());
    }

    @Test
    public void refill() throws BankException {
        debitAccount1.refill(10000d, Instant.now());
        Assertions.assertEquals(15000d, debitAccount1.getBalance());
    }

    @Test
    public void cancelRefill() throws BankException {
        debitAccount1.refill(10000d, Instant.now());
        debitAccount1.undoTransaction(debitAccount1.getTransactions().get(0).getId());
        Assertions.assertEquals(10000d, debitAccount1.getBalance());
    }

    @Test
    public void payPercents() throws BankException {
        Instant dateMonthAhead = Instant.now().plus(30, ChronoUnit.DAYS);
        centralBank.notify(dateMonthAhead);
        Assertions.assertEquals(5017, Math.round(debitAccount1.getBalance()));
    }

    @Test
    public void creditCommission() throws BankException {
        Account creditAccount = bank.addNewAccount(client1, AccountType.Credit, null);
        creditAccount.withdrawal(5000d, Instant.now());
        Instant dateMonthAhead = Instant.now().plus(15, ChronoUnit.DAYS);
        centralBank.notify(dateMonthAhead);
        Assertions.assertEquals(-6000, Math.round(creditAccount.getBalance()));
    }

    @Test
    public void transfer() throws BankException {
        debitAccount1.transfer(2000d, debitAccount2, Instant.now());
        Assertions.assertEquals(3000, debitAccount1.getBalance());
        Assertions.assertEquals(3000, debitAccount2.getBalance());
    }

    @Test
    public void CheckSubscription()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        bank.subscribeClient(debitAccount1);
        bank.changeDebitPercent(3d);
        Assertions.assertEquals("Debit percentage changed\n", out.toString());
    }
}
