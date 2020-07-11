package fiap.stock.portal.order.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.Order;
import fiap.stock.portal.order.domain.OrderService;
import fiap.stock.portal.order.domain.OrderStatus;
import fiap.stock.portal.order.domain.exception.OrderNotFoundException;
import fiap.stock.portal.product.domain.Product;
import fiap.stock.portal.stockorder.domain.StockOrderService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrderUseCase {

    public static class AddressPayload {
        @JsonProperty("zip_code")
        String zipCode;

        @JsonProperty
        String complement;

        @JsonProperty
        String city;

        @JsonProperty
        String state;

        @JsonProperty
        String country;

        AddressPayload(String zipCode, String complement, String city, String state, String country) {
            this.zipCode = zipCode;
            this.complement = complement;
            this.city = city;
            this.state = state;
            this.country = country;
        }
    }

    public static class ProductPayload {
        @JsonProperty
        String code;

        @JsonProperty
        Integer quantity;

        ProductPayload(String code, Integer quantity) {
            this.code = code;
            this.quantity = quantity;
        }
    }

    public static class OrderPayload {
        @JsonProperty("entry_time")
        LocalDateTime entryTime;

        @JsonProperty
        String code;

        @JsonProperty
        OrderStatus status;

        @JsonProperty
        List<ProductPayload> products;

        @JsonProperty
        AddressPayload address;

        OrderPayload(String code, LocalDateTime entryTime, OrderStatus status, List<ProductPayload> products, AddressPayload address) {
            this.entryTime = entryTime;
            this.status = status;
            this.code = code;
            this.products = products;
            this.address = address;
        }
    }

    private final OrderService orderService;
    private final StockOrderService stockOrderService;

    public OrderUseCase(OrderService orderService, StockOrderService stockOrderService) {
        this.orderService = orderService;
        this.stockOrderService = stockOrderService;
    }

    public OrderPayload insertNewOrderToAClient(String loginId, OrderPayload orderPayload) throws InvalidSuppliedDataException {
        orderService.validLoginId(loginId);

        List<Product> orderProducts = Optional.ofNullable(orderPayload.products)
                .map(List::stream)
                .orElse(Stream.empty())
                .map(productPayload -> new Product(
                        productPayload.code,
                        productPayload.quantity
                ))
                .collect(Collectors.toList());
        orderService.validProductList(orderProducts);

        Address address = Optional.ofNullable(orderPayload.address)
                .map(addressPayload -> new Address(
                        addressPayload.zipCode,
                        addressPayload.complement,
                        addressPayload.city,
                        addressPayload.state,
                        addressPayload.country
                ))
                .orElse(null);
        orderService.validAddress(address);

        String orderCode = orderService.figureOutNewOrderCode();

        Order order = new Order(
                loginId,
                orderCode,
                orderProducts,
                address,
                OrderStatus.WAITING_FOR_ANSWER,
                LocalDateTime.now()
        );

        orderService.save(order);

        stockOrderService.postOrderToStock(loginId, order);

        return getOrderPayload(order);
    }

    public void updateClientOrderStatus(String loginId, String orderCode, OrderPayload orderPayload) throws InvalidSuppliedDataException, OrderNotFoundException {
        orderService.validLoginId(loginId);

        orderService.validCode(orderCode);

        orderService.validOrderStatus(orderPayload.status);

        orderService.updateOrderStatus(orderCode, orderPayload.status);
    }

    public List<OrderPayload> findAllOrders(String loginId) throws InvalidSuppliedDataException {
        orderService.validLoginId(loginId);

        List<Order> orderList = orderService.findAllByLoginId(loginId);

        return orderList.stream()
                .map(this::getOrderPayload)
                .collect(Collectors.toList());
    }

    private OrderPayload getOrderPayload(Order order) {
        List<ProductPayload> productPayloadList = order.getProducts()
                .stream()
                .map(product -> new ProductPayload(
                        product.getCode(),
                        product.getQuantity()
                ))
                .collect(Collectors.toList());
        AddressPayload addressPayload = new AddressPayload(
                order.getAddress().getZipCode(),
                order.getAddress().getComplement(),
                order.getAddress().getCity(),
                order.getAddress().getState(),
                order.getAddress().getCountry()
        );
        return new OrderPayload(
                order.getCode(),
                order.getEntryTime(),
                order.getOrderStatus(),
                productPayloadList,
                addressPayload
        );
    }

}
