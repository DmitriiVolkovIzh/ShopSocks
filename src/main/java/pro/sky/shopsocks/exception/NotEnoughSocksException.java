package pro.sky.shopsocks.exception;

public class NotEnoughSocksException extends RuntimeException{
    public NotEnoughSocksException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Нет на складе, данных носков";
    }
}
