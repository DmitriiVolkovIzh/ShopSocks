package exception;

public class InvalidValueException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Не верно введены данные";
    }
}
