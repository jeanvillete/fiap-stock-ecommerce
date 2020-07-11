package fiap.sample.login.user.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends MongoRepository<User, String> {

    Optional<Integer> countByLoginAndType(String login, UserType type);

    Optional<User> findByLoginAndType(String login, UserType type);

}
