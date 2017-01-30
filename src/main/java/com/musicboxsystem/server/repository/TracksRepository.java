package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Tracks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-29.
 */
@Repository
public interface TracksRepository extends MongoRepository<Tracks, String> {
    List<Tracks> findByBandsId(String Id);
}
