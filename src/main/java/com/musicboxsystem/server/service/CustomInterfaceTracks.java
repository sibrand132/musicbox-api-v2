package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Tracks;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
public interface CustomInterfaceTracks {
    List<Tracks> findByBandsId(String id);

}
