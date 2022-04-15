import model.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.OwnerRepository;
import service.OwnerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {
    private OwnerRepository ownerRepository;
    private OwnerService ownerService;
    private Owner owner;
    private UUID ownerId;

    @BeforeEach
    void before() {
        ownerId = UUID.randomUUID();
        owner = new Owner(ownerId, "Asd", LocalDate.now(), new ArrayList<>());
        ownerRepository = mock(OwnerRepository.class);
        ownerService = new OwnerService(ownerRepository);
    }

    @Test
    void addOwner() {
        Owner ownerFromService = ownerService.addOwner("Asd", LocalDate.now());
        verify(ownerRepository, Mockito.times(1)).save(ownerFromService);
    }

    @Test
    void getOwnerById() {
        Mockito.when(ownerRepository.findById(Mockito.any())).thenReturn(Optional.of(owner));
        Owner ownerFromService = ownerService.getOwnerById(ownerId);
        Assertions.assertEquals(owner, ownerFromService);
    }
}
