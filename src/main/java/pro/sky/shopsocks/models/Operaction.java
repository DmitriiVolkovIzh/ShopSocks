package pro.sky.shopsocks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operaction {
    public enum TypeOfOperaction {
        ACCEPTANCE, WRITING_OFF, RELEASING
    }


    private TypeOfOperaction typeOfOperaction;

    private String dateOfOperation;

    private SocksPrototype socks;

    private String description;

}
