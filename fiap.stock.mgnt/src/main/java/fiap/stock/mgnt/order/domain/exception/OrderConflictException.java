package fiap.stock.mgnt.order.domain.exception;

public class OrderConflictException extends Exception {

    public OrderConflictException(String message) {
        super(message);
    }
}
