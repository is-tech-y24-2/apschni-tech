package controller;

import dto.CatDtoRequest;
import dto.CatDtoResponse;
import lombok.RequiredArgsConstructor;
import model.Cat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.CatService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class CatController {

    private final CatService catService;

    @GetMapping("cat/{id}")
    public CatDtoResponse getById(@PathVariable UUID id) {
        Cat cat = catService.getCatById(id);

        if (cat == null) {
            return null;
        }

        return CatDtoResponse.builder()
                .name(cat.getName())
                .birthdate(cat.getBirthdate())
                .friends(cat.getFriends())
                .breed(cat.getBreed())
                .color(cat.getColor())
                .ownerId(cat.getOwner().getId())
                .build();
    }

    @DeleteMapping("cat/{id}")
    public void deleteById(@PathVariable UUID id) {
        catService.deleteCatById(id);
    }

    @PostMapping("cat/")
    public Cat add(@RequestBody CatDtoRequest cat) {
        return catService.addCat(cat.getName(), cat.getBirthdate(), cat.getBreed(), cat.getColor(), cat.getOwnerId());
    }

    @PatchMapping("cat/")
    public void update(@RequestBody Cat cat) {
        catService.updateCat(cat);
    }

    @PostMapping("cat/{id}/friends/add")
    public void addFriend(@PathVariable UUID id, @RequestParam UUID friendId) {
        catService.addFriend(id, friendId);
    }
}
