package com.musicboxsystem.server.rest;

import com.mongodb.gridfs.GridFSDBFile;
import com.musicboxsystem.server.domain.*;
import com.musicboxsystem.server.service.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
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
import sun.misc.IOUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Null;
import javax.xml.bind.DatatypeConverter;

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
        response.clear();

        String password = usersEntity.getPass();
        String hashedPass = passwordEncoder().encodePassword(password, this.getSalt());

        if(!usersService.passConfirmation(usersEntity.getPass(),usersEntity.getPassconf()))
        {
            ObjectError errorPass = new ObjectError("pass","Invalid password");
            bindingResult.addError(errorPass);

            ObjectError errorPassconf = new ObjectError("passconf","Invalid password ");
            bindingResult.addError(errorPassconf);


            response.put("passconf", "Invalid password confirmation");
            response.put("pass", "Invalid password");
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
            usersEntity.setPass(hashedPass);
            usersService.update(usersEntity,id);
            response.put("message", "Success");
        }

//////////////
        return response;
    }



////////////////////////////Members///////////////////////////////////

    @RequestMapping(method = RequestMethod.GET, value = "/getMembers")
    public @ResponseBody
    List<Members> findAllMembers(){
        return membersService.getObj();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsersByBandsId/{id}")
    public @ResponseBody List<Users> findUsersByBandsId(@PathVariable String id){
        List<Members> members = membersService.findByBandsId(id);
        List<Users> users = new ArrayList<>();
        for (Members member:members)
        {
            users.add(usersService.findById(member.getUsersId()));
        }
        return users;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getMembersByBandsId/{id}")
    public @ResponseBody List<Members> findMembersByBandsId(@PathVariable String id){
        List<Members> members = membersService.findByBandsId(id);
        return members;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBandByUsersId/{id}")
    public @ResponseBody List<Bands> find(@PathVariable String id){
        List<Members> members = membersService.findByUsersId(id);
        List<Bands> bands = new ArrayList<Bands>();
        for (Members member:members)
        {
            bands.add(bandsService.findById(member.getBandsId()));
        }
        return bands;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkToken")
    public @ResponseBody Map<String,Object> checkProfile(@Valid @RequestBody Token token){
        response.clear();
        try {

           Jwts.parser().setSigningKey(this.salt).parseClaimsJws(token.getToken());
            response.put("token", token.getToken());
            response.put("message", "Success");
        } catch (SignatureException e) {
            response.put("token", "fake");
            response.put("message", e.getMessage());
        }

        return response;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/saveMember/{idBand}/{idUser}")
    public @ResponseBody
    Map<String, Object> create(@Valid @RequestBody Members membersEntity, BindingResult bindingResult, @PathVariable String idBand, @PathVariable String idUser){

            membersService.createMember(idBand,idUser);
            response.put("message", "Success");

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteMembers/{id}/{bandId}")
    public @ResponseBody void deleteMembers( @PathVariable String id, @PathVariable String bandId){
        List<Members> members = membersService.findByUsersId(id);
        List<Members> membersList = membersService.findByBandsId(bandId);
        for(Members member : members){
            for(Members temp : membersList){
                if (member.getId().equals(temp.getId()))
                    membersService.delete(member.getId());
            }
        }
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

            tracksService.create(tracksEntity);
            response.put("message", "Success");


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
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/bandsLogo/"+ bandsId + "/" + bandsId + ".png" )));
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
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/albumsLogo/"+ albumsId + "/" + albumsId + ".png" )));
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




    @RequestMapping(value="/uploadUsersAvatar/{usersId}", method=RequestMethod.POST)
    public @ResponseBody String uploadUsersAvatar(@RequestParam("file") MultipartFile file, @PathVariable String usersId){
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                File dir = new File("uploads/usersAvatar/"+usersId);
                dir.mkdir();
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("uploads/usersAvatar/"+ usersId + "/" + usersId +".png" )));
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

    @RequestMapping(value = "/checkAvatar/{userId}", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> checkAvatar(@PathVariable String userId){
        response.clear();
        File file = new File("uploads/usersAvatar/"+userId);
        File [] files = file.listFiles();
        if(files.length==0)
            response.put("avatar", false);
        else
            response.put("avatar", true);
        return response;
    }





    @RequestMapping("/getUsersAvatar/{personId}")
    @ResponseBody
    public HttpEntity<byte[]> getUsersAvatar(@PathVariable String personId) throws IOException {
        byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File("uploads/usersAvatar/"+ personId + "/"+personId+ ".png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(image.length);
        return new HttpEntity<byte[]>(image, headers);
    }

    @RequestMapping("/getBandLogo/{bandId}")
    @ResponseBody
    public HttpEntity<byte[]> getBandsLogo(@PathVariable String bandId) throws IOException {
        byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File("uploads/bandsLogo/"+ bandId + "/"+bandId+ ".png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(image.length);
        return new HttpEntity<byte[]>(image, headers);
    }

    @RequestMapping(value = "/checkBandLogo/{bandId}", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> checkBandLogo(@PathVariable String bandId){
        response.clear();
        File file = new File("uploads/bandsLogo/"+bandId);
        File [] files = file.listFiles();
        try{
            if(files.length==0)
                response.put("logo", false);
            else
                response.put("logo", true);
        }
        catch (NullPointerException e){
            response.put("logo", false);
        }

        return response;
    }

    @RequestMapping("/getAlbumsLogo/{albumId}")
    @ResponseBody
    public HttpEntity<byte[]> getAlbumsLogo(@PathVariable String albumId) throws IOException {
        byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File("uploads/albumsLogo/"+ albumId + "/"+albumId+ ".png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(image.length);
        return new HttpEntity<byte[]>(image, headers);
    }

    @RequestMapping(value = "/checkAlbumLogo/{albumId}", method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> checkAlbumLogo(@PathVariable String albumId){
        response.clear();
        File file = new File("uploads/albumsLogo/"+albumId);
        File [] files = file.listFiles();
        if(files.length==0)
            response.put("logo", false);
        else
            response.put("logo", true);
        return response;
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
