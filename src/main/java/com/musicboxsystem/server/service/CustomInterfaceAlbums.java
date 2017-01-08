package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Albums;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
public interface CustomInterfaceAlbums {
    List<Albums> findByBandsId(String id);

}
