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

    @Override
    public Bands findById(String id) {
        return null;
    }

    @Override
    public Bands update(Bands obj) {
        return null;
    }
}
