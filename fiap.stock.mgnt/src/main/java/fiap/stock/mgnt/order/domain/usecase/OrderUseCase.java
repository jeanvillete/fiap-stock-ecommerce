package fiap.stock.mgnt.order.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.Order;
import fiap.stock.mgnt.order.domain.OrderProduct;
import fiap.stock.mgnt.order.domain.OrderService;
import fiap.stock.mgnt.order.domain.OrderStatus;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.order.domain.exception.OrderIsNotWaitingForResponseException;
import fiap.stock.mgnt.order.domain.exception.OrderNotFoundException;
import fiap.stock.mgnt.portalorder.domain.PortalOrderService;
import fiap.stock.mgnt.product.domain.Product;
import fiap.stock.mgnt.product.domain.ProductService;
import fiap.stock.mgnt.product.domain.exception.ProductNotFoundException;
import fiap.stock.mgnt.summarizedproduct.domain.SummarizedProduct;
import fiap.stock.mgnt.summarizedproduct.domain.SummarizedProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrderUseCase {

    public static class ProductPayload {
        @JsonProperty
        String code;

        @JsonProperty
        Integer quantity;

        public ProductPayload(String code, Integer quantity) {
            this.code = code;
            this.quantity = quantity;
        }
    }

    public static class OrderPayload {
        @JsonProperty
        String code;

        @JsonProperty
        List<ProductPayload> products;

        @JsonProperty
        OrderStatus orderStatus;

        public OrderPayload(String code, List<ProductPayload> products, OrderStatus orderStatus) {
            this.code = code;
            this.products = products;
            this.orderStatus = orderStatus;
        }
    }

    private final OrderService orderService;
    private final ProductService productService;
    private final SummarizedProductService summarizedProductService;
    private final PortalOrderService portalOrderService;

    public OrderUseCase(OrderService orderService, ProductService productService, SummarizedProductService summarizedProductService, PortalOrderService portalOrderService) {
        this.orderService = orderService;
        this.productService = productService;
        this.summarizedProductService = summarizedProductService;
        this.portalOrderService = portalOrderService;
    }

    public OrderPayload insertNewOrder(String loginId, OrderPayload orderPayload) throws InvalidSuppliedDataException, OrderConflictException {
        orderService.validLoginId(loginId);

        orderService.validCode(orderPayload.code);
        orderService.checkForConflictOnInsert(orderPayload.code);

        List<Product> detachedProductList = Optional.ofNullable(orderPayload.products)
                .map(List::stream)
                .orElse(Stream.empty())
                .map(productPayload -> new Product(productPayload.code, productPayload.quantity))
                .collect(Collectors.toList());
        productService.validProductList(detachedProductList);

        Order order = new Order(
                loginId,
                orderPayload.code,
                OrderStatus.WAITING_FOR_ANSWER
        );
        Set<OrderProduct> mappedOrdersAndProducts = detachedProductList.stream()
                .map(detachedProduct -> {
                    try {
                        Product attachedProduct = productService.findByCode(detachedProduct.getCode());
                        return new OrderProduct(order, attachedProduct, detachedProduct.getQuantity());
                    } catch (ProductNotFoundException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toSet());
        order.setProducts(mappedOrdersAndProducts);
        orderService.save(order);

        return getOrderPayload(order);
    }

    public List<OrderPayload> findAllOrders(String loginId) throws InvalidSuppliedDataException {
        orderService.validLoginId(loginId);

        List<Order> allOrderList = orderService.findAllOrders();

        return allOrderList.stream()
                .map(this::getOrderPayload)
                .collect(Collectors.toList());
    }

    public OrderPayload approveClientOrder(String loginId, String orderCode) throws InvalidSuppliedDataException, OrderNotFoundException, OrderIsNotWaitingForResponseException {
        orderService.validLoginId(loginId);

        orderService.checkOrderIsWaitingForApproval(orderCode);

        Order order = orderService.findByCode(orderCode);

        List<Product> productList = order.getProducts()
                .stream()
                .map(OrderProduct::getProduct)
                .collect(Collectors.toList());
        productService.validProductList(productList);

        order.setStatus(OrderStatus.APPROVED);
        orderService.save(order);

        productList.forEach(product -> {
            try {
                SummarizedProduct summarizedProduct = summarizedProductService.summarizeProduct(product);
                summarizedProductService.postToStockPortal(loginId, summarizedProduct);
            } catch (InvalidSuppliedDataException exception) {
                throw new RuntimeException(exception);
            }
        });

        portalOrderService.postUpdatedOrderStatus(loginId, order);

        return getOrderPayload(order);
    }

    public OrderPayload rejectClientOrder(String loginId, String orderCode) throws InvalidSuppliedDataException, OrderNotFoundException, OrderIsNotWaitingForResponseException {
        orderService.validLoginId(loginId);

        orderService.checkOrderIsWaitingForApproval(orderCode);

        Order order = orderService.findByCode(orderCode);
        order.setStatus(OrderStatus.REJECTED);
        orderService.save(order);

        portalOrderService.postUpdatedOrderStatus(loginId, order);

        return getOrderPayload(order);
    }

    private OrderPayload getOrderPayload(Order order) {
        List<ProductPayload> productsPayload = order.getProducts()
                .stream()
                .map(orderProduct -> new ProductPayload(
                        orderProduct.getProduct().getCode(),
                        orderProduct.getQuantity()
                ))
                .collect(Collectors.toList());
        return new OrderPayload(
                order.getCode(),
                productsPayload,
                order.getStatus()
        );
    }

}
