package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Members;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
public interface CustomInterfaceMembers {
    List<Members> findByBandsId(String id);

}
