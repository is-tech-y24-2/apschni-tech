package dto;

import lombok.Builder;
import lombok.Data;
import model.Cat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OwnerDtoResponse {
    private String name;
    private LocalDate birthdate;
    private List<Cat> cats;
}
