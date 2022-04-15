package service;

import lombok.RequiredArgsConstructor;
import model.Cat;
import model.Color;
import model.Owner;
import org.springframework.stereotype.Service;
import repository.CatRepository;
import repository.OwnerRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    public void updateCat(Cat cat) {
        catRepository.save(cat);
    }

    public Cat getCatById(UUID id) {
        return catRepository.findById(id).orElse(null);
    }

    public void deleteCatById(UUID catId) {
        catRepository.deleteById(catId);
    }

    public Cat addCat(String name, LocalDate birthdate, String breed, Color color, UUID ownerId) {
        Optional<Owner> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            return null;
        }

        Cat cat = new Cat(name, birthdate, breed, color, owner.get());
        catRepository.save(cat);
        return cat;
    }

    public void addFriend(UUID catId, UUID friendId) {
        Optional<Cat> catOptional = catRepository.findById(catId);
        Optional<Cat> friendOptional = catRepository.findById(friendId);
        if (!catOptional.isPresent() || !friendOptional.isPresent()) {
            return;
        }

        Cat cat = catOptional.get();
        Cat friend = friendOptional.get();
        cat.getFriends().add(friend);
        catRepository.save(cat);
    }
}
