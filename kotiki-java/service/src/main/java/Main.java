import dao.OwnerDAO;
import model.Owner;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        OwnerService ownerService = new OwnerService(new OwnerDAO());
        Owner asd = ownerService.addOwner("asd", LocalDate.now());
    }
}
