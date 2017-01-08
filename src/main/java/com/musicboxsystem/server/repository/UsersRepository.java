package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Repository
public interface UsersRepository extends MongoRepository<Users,String> {
}
