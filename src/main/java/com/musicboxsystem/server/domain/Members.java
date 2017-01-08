package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Document
public class Members {

    @Id
    public String id;
    public String BandsId;
    public String UsersId;

}
