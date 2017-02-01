package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.repository.BandsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
@Service
public class BandsService  implements ServiceInterface<Bands>{

    private BandsRepository bandsRepository;

    @Autowired
    public BandsService(BandsRepository bandsRepository) {
        this.bandsRepository = bandsRepository;
    }

    @Override
    public List<Bands> getObj() {
        List<Bands> bandsList = bandsRepository.findAll();
        return convertToDTOs(bandsList);
    }

    private List<Bands> convertToDTOs(List<Bands> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Bands convertToDTO(Bands model){
        Bands dto= new Bands();
        dto.setId(model.getId());
        dto.setAbout(model.getAbout());
        dto.setEstablished(model.getEstablished());
        dto.setLeader(model.getLeader());
        dto.setName(model.getName());
        dto.setStatus(model.getStatus());
        return dto;
    }


    @Override
    public Bands create(Bands obj) {
        return bandsRepository.save(obj);
    }


    public boolean bandExist(Bands obj){
        List<Bands> bandsList = bandsRepository.findAll();
        for (Bands band: bandsList) {
            if(band.getName().equals(obj.getName()))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public Bands findById(String id) {
        Bands bands = bandsRepository.findOne(id);
        return bands;
    }

    @Override
    public Bands update(Bands obj, String id) {
        Bands bands = bandsRepository.findOne(id);
        bands.setStatus(obj.getStatus());
        bands.setAbout(obj.getAbout());
        bands.setName(obj.getName());
        bands.setLeader(obj.getLeader());

        return bandsRepository.save(bands);
    }

    @Override
    public void delete(String obj) {
        bandsRepository.delete(obj);
    }
}
