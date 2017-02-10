package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.domain.Songs;

import com.musicboxsystem.server.domain.Users;
import com.musicboxsystem.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-29.
 */
@Service
public class SongsService implements ServiceInterface<Songs>, CustomInterfaceSongs{

    public SongsRepository songsRepository;

    
    @Autowired
    public SongsService(SongsRepository songsRepository) {
        this.songsRepository = songsRepository;
    }

    @Override
    public List<Songs> getObj() {
        List<Songs> songsList = songsRepository.findAll();
        return convertToDTOs(songsList);
    }

    private List<Songs> convertToDTOs(List<Songs> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Songs convertToDTO(Songs songs){
        Songs dto = new Songs();
        dto.setId(songs.getId());
        dto.setBandsId(songs.getBandsId());
        dto.setDate(songs.getDate());
        dto.setAlbumsId(songs.getAlbumsId());
        dto.setTitle(songs.getTitle());
        dto.setUploaded(songs.getUploaded());
        dto.setFileName(songs.getFileName());
        return dto;
    }

    @Override
    public Songs create(Songs obj) {
        return songsRepository.save(obj);
    }

    @Override
    public Songs findById(String id) {
        Songs songs = songsRepository.findOne(id);
        return songs;
    }


    @Override
    public Songs update(Songs obj, String id) {
        Songs songs = songsRepository.findOne(id);
        songs.setAlbumsId(obj.getAlbumsId());
        songs.setTitle(obj.getTitle());
        songs.setDate(obj.getDate());
        songs.setBandsId(obj.getBandsId());
        songs.setUploaded(obj.getUploaded());
        songs.setFileName(obj.getFileName());
        return songsRepository.save(songs);
    }

    @Override
    public void delete(String obj) {
        songsRepository.delete(obj);
    }

    @Override
    public List<Songs> findByBandsId(String id) {
        List<Songs> songsList = songsRepository.findByBandsId(id);
        return  convertToDTOs(songsList);
    }

    @Override
    public List<Songs> findByAlbumsId(String id) {
        return null;
    }



}
