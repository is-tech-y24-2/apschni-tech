import dao.OwnerDAO;
import model.Owner;

import java.time.LocalDate;
import java.util.UUID;

public class OwnerService {
    private OwnerDAO ownerDAO;

    public OwnerService(OwnerDAO ownerDAO) {
        this.ownerDAO = ownerDAO;
    }

    public void updateOwner(Owner owner){
        ownerDAO.save(owner);
    }

    public Owner getOwnerById(UUID id){
        return ownerDAO.findById(id);
    }

    public void deleteOwner(Owner owner){
        ownerDAO.delete(owner);
    }

    public Owner addOwner(String name, LocalDate birthdate){
        Owner owner = new Owner(name, birthdate);
        ownerDAO.save(owner);
        return owner;
    }
}
