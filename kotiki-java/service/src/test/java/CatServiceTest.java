import dao.CatDAO;
import model.Cat;
import model.Color;
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

public class CatServiceTest {
    private CatDAO catDAO;
    private CatService catService;
    private Cat cat;
    private UUID catId;
    private UUID ownerId;

    @BeforeEach
    void before(){
        catId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        Owner owner = new Owner(ownerId, "Asd", LocalDate.now(), new ArrayList<>());
        cat = new Cat(catId, "Asd", LocalDate.now(),"Asd1", Color.RED, new Owner("Asd", LocalDate.now()), new ArrayList<>());
        catDAO = mock(CatDAO.class);
        catService = new CatService(catDAO);
    }

    @Test
    void addCat(){
        Cat catFromService = catService.addCat("Asd", LocalDate.now(),"Asd1", Color.RED, new Owner("Asd", LocalDate.now()));
        verify(catDAO, Mockito.times(1)).save(catFromService);
    }

    @Test
    void getCatById(){
        Mockito.when(catDAO.findById(Mockito.any())).thenReturn(cat);
        Cat catFromService = catService.getCatById(catId);
        Assertions.assertEquals(cat, catFromService);
    }
}
