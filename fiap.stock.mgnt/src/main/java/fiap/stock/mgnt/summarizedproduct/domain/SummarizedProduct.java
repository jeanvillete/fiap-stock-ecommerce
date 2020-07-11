package fiap.stock.mgnt.summarizedproduct.domain;

import java.math.BigDecimal;

public class SummarizedProduct {

    private String code;
    private String description;
    private BigDecimal price;
    private Integer quantity;

    public SummarizedProduct(String code, String description, BigDecimal price, Integer quantity) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
