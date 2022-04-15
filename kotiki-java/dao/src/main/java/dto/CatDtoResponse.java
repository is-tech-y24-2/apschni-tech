package dto;

import lombok.Builder;
import lombok.Data;
import model.Cat;
import model.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CatDtoResponse {
    private String name;
    private LocalDate birthdate;
    private String breed;
    private Color color;
    private UUID ownerId;
    private List<Cat> friends;
}
