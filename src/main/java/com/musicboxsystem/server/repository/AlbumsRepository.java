package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Albums;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
@Repository
public interface AlbumsRepository extends MongoRepository<Albums,String> {
    List<Albums> findByBandsId(String Id);
}
