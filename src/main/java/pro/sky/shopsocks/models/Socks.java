package pro.sky.shopsocks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.shopsocks.models.enums.Color;


import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socks {
    @NotBlank
    @NotEmpty
    private Color color;
    private double reallySize;
    @Positive
    @Max(value = 100)
    @Min(0)
    private int composition;
}

