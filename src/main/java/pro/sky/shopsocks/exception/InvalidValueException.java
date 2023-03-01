package pro.sky.shopsocks.exception;

public class InvalidValueException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Не верно введены данные";
    }
}
