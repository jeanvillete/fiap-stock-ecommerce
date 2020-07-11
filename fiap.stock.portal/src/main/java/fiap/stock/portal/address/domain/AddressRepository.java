package fiap.stock.portal.address.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface AddressRepository extends MongoRepository<Address, String> {

    Optional<List<Address>> findAllByLoginId(String loginId);

    Optional<Integer> countByLoginIdAndId(String loginId, String id);

}
