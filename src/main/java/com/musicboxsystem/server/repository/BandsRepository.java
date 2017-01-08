package com.musicboxsystem.server.repository;

import com.musicboxsystem.server.domain.Bands;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Repository
public interface BandsRepository extends MongoRepository<Bands, String >{
}
