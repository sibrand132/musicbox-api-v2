package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.domain.Tracks;
import com.musicboxsystem.server.domain.Users;
import com.musicboxsystem.server.repository.BandsRepository;
import com.musicboxsystem.server.repository.MembersRepository;
import com.musicboxsystem.server.repository.TracksRepository;
import com.musicboxsystem.server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-29.
 */
@Service
public class TracksService implements ServiceInterface<Tracks>, CustomInterfaceTracks{

    public TracksRepository tracksRepository;
    public MembersRepository membersRepository;

    @Autowired
    public TracksService(TracksRepository tracksRepository, MembersRepository membersRepository) {
        this.tracksRepository = tracksRepository;
        this.membersRepository = membersRepository;
    }

    @Override
    public List<Tracks> getObj() {
        List<Tracks> tracksList = tracksRepository.findAll();
        return convertToDTOs(tracksList);
    }

    private List<Tracks> convertToDTOs(List<Tracks> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Tracks convertToDTO(Tracks tracks){
        Tracks dto = new Tracks();
        dto.setId(tracks.getId());
        dto.setBandsId(tracks.getBandsId());
        dto.setMembersId(tracks.getMembersId());
        dto.setComment(tracks.getComment());
        dto.setDate(tracks.getDate());
        dto.setInstrument(tracks.getInstrument());
        dto.setUploaded(tracks.getUploaded());
        dto.setFileName(tracks.getFileName());
        return dto;
    }

    @Override
    public Tracks create(Tracks obj) {
       return tracksRepository.save(obj);
    }

    @Override
    public Tracks findById(String id) {
        Tracks tracks = tracksRepository.findOne(id);
        return tracks;
    }

    @Override
    public Tracks update(Tracks obj, String id) {
        Tracks tracks = tracksRepository.findOne(id);
        tracks.setInstrument(obj.getInstrument());
        tracks.setComment(obj.getComment());
        tracks.setDate(obj.getDate());
        tracks.setUploaded(obj.getUploaded());
        tracks.setFileName(obj.getFileName());
        return tracksRepository.save(tracks);
    }

    @Override
    public void delete(String obj) {
    tracksRepository.delete(obj);
    }

    public void deleteAll(String id){
        List<Tracks> tracksList = tracksRepository.findByBandsId(id);
        for(Tracks album : tracksList){
            tracksRepository.delete(album);
        }
    }
    
    @Override
    public List<Tracks> findByBandsId(String id) {
        List<Tracks> tracksList = tracksRepository.findByBandsId(id);
        return  convertToDTOs(tracksList);
    }


    @Override
    public List<Tracks> findByMembersId(String id) {
        List<Tracks> tracksList = tracksRepository.findByMembersId(id);
        return  convertToDTOs(tracksList);
    }
}
