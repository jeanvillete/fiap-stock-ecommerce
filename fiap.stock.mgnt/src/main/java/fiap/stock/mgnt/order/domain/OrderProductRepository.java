package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    @Query(value = "SELECT SUM( ordprod.quantity ) FROM OrderProduct ordprod JOIN ordprod.product prd JOIN ordprod.order ord WHERE prd = :product AND ord.status = :orderStatus")
    Optional<Integer> sumApprovedOrdersForSpecificProduct(Product product, OrderStatus orderStatus);

}
