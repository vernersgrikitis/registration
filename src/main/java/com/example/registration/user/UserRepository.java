package com.example.registration.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    void deleteUserByEmail(String email);

    boolean existsUsersByEmail(String email);

}
