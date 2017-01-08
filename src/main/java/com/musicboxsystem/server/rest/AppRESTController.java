package com.musicboxsystem.server.rest;

import com.musicboxsystem.server.domain.Albums;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.service.AlbumsService;
import com.musicboxsystem.server.service.BandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-08.
 */
@RestController
@RequestMapping("/api/bands")
public class AppRESTController {

    private final AlbumsService albumsService;
    private final BandsService bandsService;

    @Autowired
    public AppRESTController(AlbumsService albumsService, BandsService bandsService) {
        this.albumsService = albumsService;
        this.bandsService = bandsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBands")
    public @ResponseBody
    List<Bands> findAll(){
        return bandsService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAlbums")
    public @ResponseBody
    List<Albums> findAllAlbums(){
        return albumsService.getObj();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveBands")
    public @ResponseBody Bands create(@RequestBody Bands bandsEntity){
        return bandsService.create(bandsEntity);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveAlbums")
    public @ResponseBody
    Albums create(@RequestBody Albums albumsEntity){
        return albumsService.create(albumsEntity);
    }



}
