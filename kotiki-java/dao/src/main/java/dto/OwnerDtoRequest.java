package dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OwnerDtoRequest {
    private String name;
    private LocalDate birthdate;
}
