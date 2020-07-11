package fiap.stock.portal.order.domain;

import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.address.domain.AddressService;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.exception.OrderNotFoundException;
import fiap.stock.portal.product.domain.Product;
import fiap.stock.portal.product.domain.ProductService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
class OrderServiceImpl implements OrderService {

    private static final String ORD_PREFIX = "ORD-";

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final AddressService addressService;

    OrderServiceImpl(OrderRepository orderRepository, ProductService productService, AddressService addressService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.addressService = addressService;
    }

    @Override
    public void validLoginId(String loginId) throws InvalidSuppliedDataException {
        if (Objects.isNull(loginId)) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }

        loginId = loginId.trim();

        if (loginId.isEmpty()) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }
    }

    @Override
    public void validCode(String code) throws InvalidSuppliedDataException {
        if (Objects.isNull(code)) {
            throw new InvalidSuppliedDataException("Argument 'code' is mandatory.");
        }

        code = code.trim();

        if (!Pattern.compile("^" + ORD_PREFIX + "[\\d]{7}$").matcher(code).matches()) {
            throw new InvalidSuppliedDataException("Argument 'code' must follow the pattern; " + ORD_PREFIX + "0000000");
        }
    }

    @Override
    public void validProductList(List<Product> products) throws InvalidSuppliedDataException {
        if (Objects.isNull(products)) {
            throw new InvalidSuppliedDataException("Argument 'products' is mandatory.");
        }

        products.forEach(product -> {
            try {
                productService.validCode(product.getCode());
                productService.validQuantity(product.getQuantity());
            } catch (InvalidSuppliedDataException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public void validAddress(Address address) throws InvalidSuppliedDataException {
        if (Objects.isNull(address)) {
            throw new InvalidSuppliedDataException("Argument 'address' is mandatory.");
        }

        addressService.validZipCode(address.getZipCode());
        addressService.validComplement(address.getComplement());
        addressService.validCity(address.getCity());
        addressService.validState(address.getState());
        addressService.validCountry(address.getCountry());
    }

    @Override
    public String figureOutNewOrderCode() {
        Long currentCountPlusOne = orderRepository.count() + 1;
        return ORD_PREFIX + String.format("%07d", currentCountPlusOne);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void validOrderStatus(OrderStatus status) throws InvalidSuppliedDataException {
        if (Objects.isNull(status)) {
            throw new InvalidSuppliedDataException("Argument 'status' is mandatory.");
        }
    }

    @Override
    public Order findByCode(String code) throws OrderNotFoundException {
        return orderRepository.findByCode(code)
                .orElseThrow(() ->
                        new OrderNotFoundException("No order could be found for the provided order code [" + code + "]")
                );
    }

    @Override
    public void updateOrderStatus(String orderCode, OrderStatus status) throws OrderNotFoundException {
        Order order = findByCode(orderCode);

        order.setOrderStatus(status);
        save(order);
    }

    @Override
    public List<Order> findAllByLoginId(String loginId) {
        return orderRepository.findAllByLoginId(loginId)
            .orElse(Collections.emptyList());
    }

}
