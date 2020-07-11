package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.OrderService;
import fiap.stock.mgnt.product.domain.exception.ProductNotFoundException;
import fiap.stock.mgnt.product.domain.exception.ProductUnavailableQuantityException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
class ProductServiceImpl implements ProductService {

    private static final String PROD_PREFIX = "PRD-";

    private final ProductRepository productRepository;
    private final OrderService orderService;

    ProductServiceImpl(ProductRepository productRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
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
    public void validPrice(BigDecimal price) throws InvalidSuppliedDataException {
        if (Objects.isNull(price)) {
            throw new InvalidSuppliedDataException("Argument 'price' is mandatory.");
        }

        boolean greaterThanZero = price.compareTo(BigDecimal.ZERO) == 1;
        if (!greaterThanZero) {
            throw new InvalidSuppliedDataException("Argument 'price' must be greater than zero.");
        }
    }

    @Override
    public void validQuantity(Integer quantity) throws InvalidSuppliedDataException {
        if (Objects.isNull(quantity)) {
            throw new InvalidSuppliedDataException("Nested argument 'quantity' for product is mandatory.");
        }

        if (quantity <= 0) {
            throw new InvalidSuppliedDataException("Nested argument 'quantity' for product must be greater than zero.");
        }
    }

    @Override
    public void validCode(String code) throws InvalidSuppliedDataException {
        if (Objects.isNull(code)) {
            throw new InvalidSuppliedDataException("Nested argument 'code' for product is mandatory.");
        }

        code = code.trim();

        if (!Pattern.compile("^" + PROD_PREFIX + "[\\d]{7}$").matcher(code).matches()) {
            throw new InvalidSuppliedDataException("Nested argument 'code' for product must follow the pattern; " + PROD_PREFIX + "0000000");
        }
    }

    @Override
    public String figureOutNewProductCode() {
        Long currentCountPlusOne = productRepository.count() + 1;
        return PROD_PREFIX + String.format("%07d", currentCountPlusOne);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Integer availableQuantity(Product product) {
        Integer quantityAlreadySold = orderService.sumApprovedOrdersForSpecificProduct(product);
        Integer availableQuantity = product.getQuantity() - quantityAlreadySold;

        return availableQuantity;
    }

    @Override
    public void ensureAvailableQuantity(String code, Integer requestedQuantity) throws ProductNotFoundException, ProductUnavailableQuantityException {
        Product product = findByCode(code);
        Integer availableQuantity = availableQuantity(product);

        if (requestedQuantity > availableQuantity) {
            throw new ProductUnavailableQuantityException(
                    "Available quantity for product [" + product.getCode() + "] is only [" + availableQuantity + "]," +
                            " but it was requested [" + requestedQuantity + "]"
            );
        }
    }

    @Override
    public void validProductList(List<Product> products) throws InvalidSuppliedDataException {
        if (Objects.isNull(products) || products.isEmpty()) {
            throw new InvalidSuppliedDataException("Argument 'products' is mandatory.");
        }

        products.forEach(product -> {
            try {
                validCode(product.getCode());
                validQuantity(product.getQuantity());
                ensureAvailableQuantity(product.getCode(), product.getQuantity());
            } catch (InvalidSuppliedDataException | ProductNotFoundException | ProductUnavailableQuantityException exception) {
                throw new RuntimeException(exception);
            }
        });
    }


    @Override
    public Product findByCode(String code) throws ProductNotFoundException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("No product found for the provided code [" + code + "]"));
    }

}
