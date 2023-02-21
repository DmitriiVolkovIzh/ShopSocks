package models.enums;

public enum Color {
    WHITE("Белые"),BLACK("Черные"),GREY("Серые"),RED("Красные"),BROWN("Коричневые");
    private String nameColor;

    Color(String nameToString) {

        this.nameColor = nameColor;
    }

    public String getNameColor() {
        return nameColor;
    }
}


