package pro.sky.shopsocks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.shopsocks.models.enums.SizeSocks;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocksPrototype {
    private Socks socks;
    private SizeSocks sizeSocks;
    @Positive
    @Max(value = 100)
    @Min(0)
    private int quantity;
}
