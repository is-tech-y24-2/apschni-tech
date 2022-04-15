package dto;

import lombok.Builder;
import lombok.Data;
import model.Color;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CatDtoRequest {
    private String name;
    private LocalDate birthdate;
    private String breed;
    private Color color;
    private UUID ownerId;
}
