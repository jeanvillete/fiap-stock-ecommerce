package fiap.stock.portal.stockorder.domain;

import fiap.stock.portal.product.domain.Product;

import java.util.List;

public class StockOrder {

    private String code;
    private List<Product> products;

    public StockOrder(String code, List<Product> products) {
        this.code = code;
        this.products = products;
    }

    public String getCode() {
        return code;
    }

    public List<Product> getProducts() {
        return products;
    }
}
