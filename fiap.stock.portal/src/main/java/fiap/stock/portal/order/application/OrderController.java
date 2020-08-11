package fiap.stock.portal.order.application;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.usecase.OrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("portal/users/{loginId}/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderUseCase.OrderPayload insertNewOrderToAClient(
            @PathVariable("loginId") String loginId,
            @RequestBody OrderUseCase.OrderPayload orderPayload)
            throws InvalidSuppliedDataException {

        return this.orderUseCase.insertNewOrderToAClient(loginId, orderPayload);
    }

    @GetMapping
    public List<OrderUseCase.OrderPayload> findAllOrders(
            @PathVariable("loginId") String loginId)
            throws InvalidSuppliedDataException {

        return orderUseCase.findAllOrders(loginId);
    }

}
