package com.musicboxsystem.server.rest;

import com.musicboxsystem.server.domain.*;
import com.musicboxsystem.server.service.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import javax.validation.constraints.Null;

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
    private final TracksService tracksService;
    private final Map<String,Object> response = new LinkedHashMap<>();



    public AppRESTController(AlbumsService albumsService, BandsService bandsService, UsersService usersService, MembersService membersService, TracksService tracksService) {
        this.albumsService = albumsService;
        this.bandsService = bandsService;
        this.usersService = usersService;
        this.membersService = membersService;
        this.tracksService = tracksService;
    }

    @Autowired
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

    private String salt = "3ebed6b2ea9ed4b3";

    public String getSalt() {
        return salt;
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


        response.clear();


        if(bandsService.bandExist(bandsEntity))
        {
            ObjectError errorEmail = new ObjectError("name","Band already exist");
            bindingResult.addError(errorEmail);
            response.put("name", "Band already exist");
        }
        if(bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();

            response.put("message", "Error");

            for(FieldError error: errors)
            {
                response.put(error.getField(),error.getDefaultMessage());
            }
        }
        else
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
        response.clear();
        String password = usersEntity.getPass();
        String hashedPass = passwordEncoder().encodePassword(password, this.getSalt());
        System.out.println(this.getSalt());

        if(!usersService.passConfirmation(usersEntity.getPass(),usersEntity.getPassconf()))
        {
            ObjectError errorPass = new ObjectError("pass","Invalid password");
            bindingResult.addError(errorPass);

            ObjectError errorPassconf = new ObjectError("passconf","Invalid password ");
            bindingResult.addError(errorPassconf);


            response.put("passconf", "Invalid password confirmation");
            response.put("pass", "Invalid password");
        }

        if(usersService.userExist(usersEntity))
        {
            ObjectError errorEmail = new ObjectError("email","User already exist");
            bindingResult.addError(errorEmail);
            response.put("email", "User already exist");
        }
        if(bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();

            response.put("message", "Error");

            for(FieldError error: errors)
            {
                response.put(error.getField(),error.getDefaultMessage());
            }
        }
        else
        {
                usersEntity.setRole("user");
                usersEntity.setPass(hashedPass);
                usersService.create(usersEntity);

                response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public @ResponseBody Map<String,Object> login(@Valid @RequestBody Login loginEntity, BindingResult bindingResult){
        response.clear();
        String password = loginEntity.getPass();
        String hashedPass = passwordEncoder().encodePassword(password, this.getSalt());



        Users usersEntity = usersService.findByEmail(loginEntity.getEmail());
        Claims customClaims = Jwts.claims();
        customClaims.put("email", usersEntity.getEmail());
        customClaims.put("role", usersEntity.getRole());
        customClaims.put("name", usersEntity.getName());
        customClaims.put("id", usersEntity.getId());

        if(!usersService.bandIdIfLeader(loginEntity.getEmail()).equals("")){
            customClaims.put("bandIdLeader", usersService.bandIdIfLeader(loginEntity.getEmail()));
        }


        String compactJws = Jwts.builder()
                .setClaims(customClaims)
                .signWith(SignatureAlgorithm.HS512, this.getSalt())
                .compact();




        if(!usersService.userExist(usersEntity) || !usersEntity.getPass().equals(hashedPass) )
        {
            response.put("message", "Error");
            response.put("email", "Invalid email and/or password");
            response.put("pass", "Invalid email and/or password");
        }
        else {

            response.put("message", "Success");
            response.put("token", compactJws);
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUsers/{id}")
    public @ResponseBody void deleteUsers( @PathVariable String id){
        usersService.delete(id);
        membersService.deleteAll(id);
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



    @RequestMapping(method = RequestMethod.GET, value = "/getTracks")
    public @ResponseBody
    List<Tracks> findAllTracks(){
        return tracksService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTracksById/{id}")
    public @ResponseBody Tracks findTracksById(@PathVariable String id){
        return tracksService.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTracksByBandsId/{id}")
    public @ResponseBody List<Tracks> findTracksByBandsId(@PathVariable String id){
        return tracksService.findByBandsId(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveTracks")
    public @ResponseBody Map<String,Object> createTracks(@Valid @RequestBody Tracks tracksEntity, BindingResult bindingResult){

        if (checkError("bands", bindingResult))
        {
            tracksService.create(tracksEntity);
            response.put("message", "Success");
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteTracks/{id}")
    public @ResponseBody void deleteTracks( @PathVariable String id){
        tracksService.delete(id);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/updateTracks/{id}")
    public @ResponseBody Map<String,Object> updateTracks(@Valid @RequestBody Tracks tracksEntity, BindingResult bindingResult, @PathVariable String id){

        if (checkError("members", bindingResult))
        {
            tracksService.update(tracksEntity, id);
            response.put("message", "Success");
        }

        return response;
    }



    //////////////////////////////////    FILE UPLOAD //////////////////////////////////////////////////////////////////

    @RequestMapping(value="/uploadBandsLogo/{bandsId}", method=RequestMethod.POST)
    public @ResponseBody String uploadBandsLogo(@RequestParam("file") MultipartFile file, @PathVariable String bandsId){
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                File dir = new File("uploads/bandsLogo/"+bandsId);
                dir.mkdir();
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/bandsLogo/"+ bandsId + "/" + name )));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    @RequestMapping(value="/uploadAlbumsLogo/{albumsId}", method=RequestMethod.POST)
    public @ResponseBody String uploadAlbumsLogo(@RequestParam("file") MultipartFile file, @PathVariable String albumsId){
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                File dir = new File("uploads/albumsLogo/"+albumsId);
                dir.mkdir();
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/albumsLogo/"+ albumsId + "/" + name )));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    @RequestMapping(value="/uploadTracks/{bandId}", method=RequestMethod.POST)
    public @ResponseBody String uploadTracks(@RequestParam("file") MultipartFile file, @PathVariable String bandId){
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                File dir = new File("uploads/tracks/"+bandId);
                dir.mkdir();
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/tracks/"+ bandId + "/" + name )));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
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
