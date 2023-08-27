package user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
