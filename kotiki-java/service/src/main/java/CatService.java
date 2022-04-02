import dao.CatDAO;
import model.Cat;
import model.Color;
import model.Owner;

import java.time.LocalDate;
import java.util.UUID;

public class CatService {
    private CatDAO catDAO;

    public CatService(CatDAO catDAO) {
        this.catDAO = catDAO;
    }

    public void updateCat(Cat cat){
        catDAO.save(cat);
    }

    public Cat getCatById(UUID id){
        return catDAO.findById(id);
    }

    public void deleteCat(Cat cat){
        catDAO.delete(cat);
    }

    public Cat addCat(String name, LocalDate birthdate, String breed, Color color, Owner owner){
        Cat cat = new Cat(name, birthdate, breed, color, owner);
        catDAO.save(cat);
        return cat;
    }

    public void addFriend(Cat cat, Cat friend){
        cat.getFriends().add(friend);
        catDAO.save(cat);
    }
}
