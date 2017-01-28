package com.musicboxsystem.server.rest;

import com.musicboxsystem.server.domain.*;
import com.musicboxsystem.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import java.io.*;
import java.util.*;

/**
 * Created by Sibrand on 2017-01-08.
 */
@RestController
@RequestMapping("/api/bands")
@MultipartConfig(fileSizeThreshold = 20971520)
public class AppRESTController {

    private final AlbumsService albumsService;
    private final BandsService bandsService;
    private final UsersService usersService;
    private final MembersService membersService;
    private final Map<String,Object> response = new LinkedHashMap<>();


    public AppRESTController(AlbumsService albumsService, BandsService bandsService, UsersService usersService, MembersService membersService) {
        this.albumsService = albumsService;
        this.bandsService = bandsService;
        this.usersService = usersService;
        this.membersService = membersService;
    }

    @Autowired



    @RequestMapping(method = RequestMethod.GET, value = "/getBands")
    public @ResponseBody
    List<Bands> findAll(){
        return bandsService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBandsById/{id}")
    public @ResponseBody Bands findById(@PathVariable String id){
        return bandsService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAlbumsByBandsId/{id}")
    public @ResponseBody List<Albums> findByBandsId(@PathVariable String id){
        return albumsService.findByBandsId(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAlbumsById/{id}")
    public @ResponseBody Albums findAlbumsById(@PathVariable String id){
        return albumsService.findById(id);
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
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteBands/{id}")
    public @ResponseBody void deleteBands( @PathVariable String id){
        bandsService.delete(id);
        albumsService.deleteAll(id);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteAlbums/{id}")
    public @ResponseBody void deleteAlbums( @PathVariable String id){
        albumsService.delete(id);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/getAlbumsByBandsId/{id}")
    public @ResponseBody
    Map<String, Object> create(@Valid @RequestBody Albums albumsEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("bands", bindingResult))
        {
            albumsService.create(albumsEntity);
            response.put("message", "Success");
        }
        return response;

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateBands/{id}")
    public @ResponseBody Map<String,Object> update(@Valid @RequestBody Bands bandsEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("bands", bindingResult))
        {
            bandsService.update(bandsEntity, id);
            response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateAlbums/{id}")
    public @ResponseBody Map<String,Object> updateAlbum(@Valid @RequestBody Albums albumsEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("albums", bindingResult))
        {
            albumsService.update(albumsEntity, id);
            response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsers")
    public @ResponseBody
    List<Users> findAllUsers(){
        return usersService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsersById/{id}")
    public @ResponseBody Users findUsersById(@PathVariable String id){
        return usersService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveUsers")
    public @ResponseBody Map<String,Object> createUsers(@Valid @RequestBody Users usersEntity, BindingResult bindingResult){

        if (checkError("bands", bindingResult))
        {
            usersService.create(usersEntity);
            response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUsers/{id}")
    public @ResponseBody void deleteUsers( @PathVariable String id){
        usersService.delete(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUsers/{id}")
    public @ResponseBody Map<String,Object> updateUsers(@Valid @RequestBody Users usersEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("albums", bindingResult))
        {
            usersService.update(usersEntity, id);
            response.put("message", "Success");
        }

        return response;
    }



    ///////Members////////
    @RequestMapping(method = RequestMethod.GET, value = "/getMembers")
    public @ResponseBody
    List<Members> findAllMembers(){
        return membersService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsersByBandsId/{id}")
    public @ResponseBody List<Users> findMembersByBandsId(@PathVariable String id){
        List<Members> members = membersService.findByBandsId(id);
        List<Users> users = new ArrayList<Users>();
        for (Members member:members)
        {
            users.add(usersService.findById(member.getUsersId()));
        }
        return users;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveMember/{idBand}/{idUser}")
    public @ResponseBody
    Map<String, Object> create(@Valid @RequestBody Members membersEntity, BindingResult bindingResult, @PathVariable String idBand, @PathVariable String idUser){

            membersService.createMember(idBand,idUser);
            response.put("message", "Success");

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteMembers/{id}")
    public @ResponseBody void deleteMembers( @PathVariable String id){
        membersService.delete(id);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/updateMembers/{id}")
    public @ResponseBody Map<String,Object> updateMembers(@Valid @RequestBody Members membersEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("members", bindingResult))
        {
            membersService.update(membersEntity, id);
            response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(value = "/upload",headers="content-type=multipart/*")
    public void uploadFile(
            @RequestParam("file") MultipartFile uploadedFileRef) {
        // Get name of uploaded file.
        String fileName = uploadedFileRef.getOriginalFilename();

        // Path where the uploaded file will be stored.
        String path = "uploads/" + fileName;

        // This buffer will store the data read from 'uploadedFileRef'
        byte[] buffer = new byte[1000];

        // Now create the output file on the server.
        File outputFile = new File(path);

        FileInputStream reader = null;
        FileOutputStream writer = null;
        int totalBytes = 0;
        try {
            outputFile.createNewFile();

            reader = (FileInputStream) uploadedFileRef.getInputStream();
            writer = new FileOutputStream(outputFile);

            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer);
                totalBytes += bytesRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



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
