package controller;

import dto.OwnerDtoRequest;
import dto.OwnerDtoResponse;
import lombok.RequiredArgsConstructor;
import model.Owner;
import org.springframework.web.bind.annotation.*;
import service.OwnerService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping("owner/{id}")
    public OwnerDtoResponse getById(@PathVariable UUID id) {
        Owner owner = ownerService.getOwnerById(id);

        if (owner == null) {
            return null;
        }

        return OwnerDtoResponse.builder()
                .name(owner.getName())
                .birthdate(owner.getBirthdate())
                .cats(owner.getCats())
                .build();
    }

    @DeleteMapping("owner/{id}")
    public void deleteById(@PathVariable UUID id) {
        ownerService.deleteOwnerById(id);
    }

    @PostMapping("owner/")
    public Owner add(@RequestBody OwnerDtoRequest owner) {
        return ownerService.addOwner(owner.getName(), owner.getBirthdate());
    }

    @PatchMapping("owner/")
    public void update(@RequestBody Owner owner) {
        ownerService.updateOwner(owner);
    }

}
