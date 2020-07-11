package fiap.stock.portal.order.domain;

import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.product.domain.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String loginId;
    private String code;
    private List<Product> products;
    private Address address;
    private OrderStatus orderStatus;
    private LocalDateTime entryTime;

    public Order() {
    }

    public Order(String loginId, String code, List<Product> products, Address address, OrderStatus orderStatus, LocalDateTime entryTime) {
        this.loginId = loginId;
        this.code = code;
        this.products = products;
        this.address = address;
        this.orderStatus = orderStatus;
        this.entryTime = entryTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}
