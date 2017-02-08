package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Songs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sibrand on 2017-02-08.
 */
@Repository
public interface SongsRepository extends MongoRepository<Songs, String> {
    List<Songs> findByBandsId(String Id);
    List<Songs> findByAlbumsId(String Id);
}
