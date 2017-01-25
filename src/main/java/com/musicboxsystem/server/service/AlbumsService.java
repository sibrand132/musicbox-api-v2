package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Albums;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.repository.AlbumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
@Service
public class AlbumsService implements ServiceInterface <Albums>, CustomInterfaceAlbums{

    public AlbumsRepository albumsRepository;

    @Autowired
    public AlbumsService(AlbumsRepository albumsRepository) {
        this.albumsRepository = albumsRepository;
    }

    @Override
    public List<Albums> getObj() {
        List<Albums> albumsList = albumsRepository.findAll();
        return convertToDTOs(albumsList);
    }

    private List<Albums> convertToDTOs(List<Albums> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Albums convertToDTO(Albums albums){
        Albums dto = new Albums();
        dto.setId(albums.getId());
        dto.setAbout(albums.getAbout());
        dto.setBandsId(albums.getBandsId());
        dto.setReleaseDate(albums.getReleaseDate());
        dto.setTitle(albums.getTitle());
        return dto;
    }

    @Override
    public Albums create(Albums obj) {
        return albumsRepository.save(obj);
    }

    @Override
    public Albums findById(String id) {

        Albums albums = albumsRepository.findOne(id);
        return albums;
    }

    @Override
    public Albums update(Albums obj, String id) {
        Albums albums = albumsRepository.findOne(id);
        albums.setTitle(obj.getTitle());
        albums.setAbout(obj.getAbout());
        albums.setBandsId(obj.getBandsId());
        albums.setReleaseDate(obj.getReleaseDate());

        return albumsRepository.save(albums);
    }

    @Override
    public void delete(String obj) {
        albumsRepository.delete(obj);
    }

    public void deleteAll(String id){
        List<Albums> albumsList = albumsRepository.findByBandsId(id);
        for(Albums album : albumsList){
            albumsRepository.delete(album);
        }
    }

    @Override
    public List <Albums> findByBandsId(String id) {
        List<Albums> albumsList = albumsRepository.findByBandsId(id);
        return  convertToDTOs(albumsList);
    }
}
