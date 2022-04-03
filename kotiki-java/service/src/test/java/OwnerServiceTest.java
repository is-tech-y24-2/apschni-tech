import dao.OwnerDAO;
import model.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OwnerServiceTest {
    private OwnerDAO ownerDAO;
    private OwnerService ownerService;
    private Owner owner;
    private UUID ownerId;

    @BeforeEach
    void before(){
        ownerId = UUID.randomUUID();
        owner = new Owner(ownerId, "Asd", LocalDate.now(), new ArrayList<>());
        ownerDAO = mock(OwnerDAO.class);
        ownerService = new OwnerService(ownerDAO);
    }

    @Test
    void addOwner(){
        Owner ownerFromService = ownerService.addOwner("Asd", LocalDate.now());
        verify(ownerDAO, Mockito.times(1)).save(ownerFromService);
    }

    @Test
    void getOwnerById(){
        Mockito.when(ownerDAO.findById(Mockito.any())).thenReturn(owner);
        Owner ownerFromService = ownerService.getOwnerById(ownerId);
        Assertions.assertEquals(owner, ownerFromService);
    }
}
