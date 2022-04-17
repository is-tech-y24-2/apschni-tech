import model.Cat;
import model.Color;
import model.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CatRepository;
import repository.OwnerRepository;
import service.CatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {
    private CatRepository catRepository;
    private OwnerRepository ownerRepository;
    private CatService catService;
    private Cat cat;
    private Owner owner;
    private UUID catId;
    private UUID ownerId;

    @BeforeEach
    void before() {
        catId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        owner = new Owner(ownerId, "Asd", LocalDate.now(), new ArrayList<>());
        cat = new Cat(catId, "Asd", LocalDate.now(), "Asd1", Color.RED, new Owner("Asd", LocalDate.now()), new ArrayList<>());
        catRepository = mock(CatRepository.class);
        ownerRepository = mock(OwnerRepository.class);
        catService = new CatService(catRepository, ownerRepository);
    }

    @Test
    void addCat() {
        Mockito.when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        Cat catFromService = catService.addCat("Asd", LocalDate.now(), "Asd1", Color.RED, owner.getId());
        verify(catRepository, Mockito.times(1)).save(catFromService);
    }

    @Test
    void getCatById() {
        Mockito.when(catRepository.findById(Mockito.any())).thenReturn(Optional.of(cat));
        Cat catFromService = catService.getCatById(catId);
        Assertions.assertEquals(cat, catFromService);
    }
}
