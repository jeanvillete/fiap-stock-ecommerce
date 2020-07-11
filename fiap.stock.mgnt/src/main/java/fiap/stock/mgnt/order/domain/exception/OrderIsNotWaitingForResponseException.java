package fiap.stock.mgnt.order.domain.exception;

public class OrderIsNotWaitingForResponseException extends Exception {

    public OrderIsNotWaitingForResponseException(String message) {
        super(message);
    }
}
