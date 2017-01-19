package com.musicboxsystem.server.rest;

import com.musicboxsystem.server.domain.Albums;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.service.AlbumsService;
import com.musicboxsystem.server.service.BandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sibrand on 2017-01-08.
 */
@RestController
@RequestMapping("/api/bands")
public class AppRESTController {

    private final AlbumsService albumsService;
    private final BandsService bandsService;
    private final Map<String,Object> response = new LinkedHashMap<>();

    @Autowired
    public AppRESTController(AlbumsService albumsService, BandsService bandsService) {
        this.albumsService = albumsService;
        this.bandsService = bandsService;
    }

//    @CrossOrigin(origins = "http://localhost:443")
    @RequestMapping(method = RequestMethod.GET, value = "/getBands")
    public @ResponseBody
    List<Bands> findAll(){
        return bandsService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBandsById/{id}")
    public @ResponseBody Bands findById(@PathVariable String id){
        return bandsService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAlbums")
    public @ResponseBody
    List<Albums> findAllAlbums(){
        return albumsService.getObj();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/saveBands")
    public @ResponseBody Map<String,Object> create(@Valid @RequestBody Bands bandsEntity, BindingResult bindingResult){

        if (checkError("bands", bindingResult))
        {
            bandsService.create(bandsEntity);
            response.put("message", "Success");
        }

        return response;
//        return bandsService.create(bandsEntity);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveAlbums")
    public @ResponseBody
    Map<String, Object> create(@Valid @RequestBody Albums albumsEntity, BindingResult bindingResult){

        if (checkError("bands", bindingResult))
        {
            albumsService.create(albumsEntity);
            response.put("message", "Success");
        }
        return response;

    }
    //Split to seperate controllers



    public boolean checkError(String type, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            System.out.println("Error");
            response.put("message", "Error");
            for(FieldError error: errors) {
                System.out.println(error.getField()+"-"+error.getDefaultMessage());
                response.put(error.getField(), error.getDefaultMessage());

            }

            return false;
        }
        else {
            return true;
        }


    }


}
