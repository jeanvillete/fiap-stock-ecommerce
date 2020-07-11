package fiap.stock.mgnt.order.application;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.order.domain.exception.OrderIsNotWaitingForResponseException;
import fiap.stock.mgnt.order.domain.exception.OrderNotFoundException;
import fiap.stock.mgnt.order.domain.usecase.OrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stock/users/{loginId}/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderUseCase.OrderPayload insertNewOrder(
            @PathVariable("loginId") String loginId,
            @RequestBody OrderUseCase.OrderPayload orderPayload)
            throws OrderConflictException, InvalidSuppliedDataException {

        return orderUseCase.insertNewOrder(loginId, orderPayload);
    }

    @GetMapping
    public List<OrderUseCase.OrderPayload> findAllOrders(
            @PathVariable("loginId") String loginId)
            throws InvalidSuppliedDataException {

        return orderUseCase.findAllOrders(loginId);
    }

    @PutMapping("{orderCode}/approve")
    public OrderUseCase.OrderPayload approveClientOrder(
            @PathVariable("loginId") String loginId,
            @PathVariable("orderCode") String orderCode)
            throws InvalidSuppliedDataException, OrderNotFoundException, OrderIsNotWaitingForResponseException {

        return orderUseCase.approveClientOrder(loginId, orderCode);
    }

    @PutMapping("{orderCode}/reject")
    public OrderUseCase.OrderPayload rejectClientOrder(
            @PathVariable("loginId") String loginId,
            @PathVariable("orderCode") String orderCode)
            throws InvalidSuppliedDataException, OrderNotFoundException, OrderIsNotWaitingForResponseException {

        return orderUseCase.rejectClientOrder(loginId, orderCode);
    }

}
