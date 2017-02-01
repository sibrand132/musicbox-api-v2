package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Members;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Repository
public interface MembersRepository extends MongoRepository<Members, String>{
    List<Members> findByBandsId(String Id);
    List<Members> findByUsersId(String id);
}
