package service;

import lombok.RequiredArgsConstructor;
import model.Owner;
import model.Role;
import model.User;
import org.springframework.stereotype.Service;
import repository.OwnerRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public void updateOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    public Owner getOwnerById(UUID id){
        return ownerRepository.findById(id).orElse(null);
    }

    public void deleteOwnerById(UUID ownerId){
        ownerRepository.deleteById(ownerId);
    }

    public Owner addOwner(String name, LocalDate birthdate, String username, String password){
        Owner owner = new Owner(name, birthdate);
        User user = new User(username, password, Role.USER, owner);
        owner.setUser(user);
        ownerRepository.save(owner);
        return owner;
    }
}
