package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.catalog.domain.Catalog;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, name = "login_id")
    private String loginId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "stock_catalog_id")
    private Catalog catalog;

    @Column(nullable = false, length = 11)
    private String code;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "entry_time")
    private LocalDateTime entryTime;

    public Product() {
    }

    public Product(String code, Integer quantity) {
        this.code = code;
        this.quantity = quantity;
    }

    public Product(String loginId, Catalog catalog, String code, BigDecimal price, Integer quantity, LocalDateTime entryTime) {
        this.loginId = loginId;
        this.catalog = catalog;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
        this.entryTime = entryTime;
    }

    public String getLoginId() {
        return loginId;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
