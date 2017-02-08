package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Songs;

import java.util.List;

/**
 * Created by Sibrand on 2017-02-08.
 */
public interface CustomInterfaceSongs {
    List<Songs> findByBandsId(String id);
    List<Songs> findByAlbumsId(String id);
}
