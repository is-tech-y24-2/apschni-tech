package service;

import lombok.RequiredArgsConstructor;
import model.Owner;
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

    public Owner addOwner(String name, LocalDate birthdate){
        Owner owner = new Owner(name, birthdate);
        ownerRepository.save(owner);
        return owner;
    }
}
