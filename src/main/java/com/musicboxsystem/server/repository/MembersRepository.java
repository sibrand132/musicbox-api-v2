package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Members;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Repository
public interface MembersRepository extends MongoRepository<Members, String>{
}
