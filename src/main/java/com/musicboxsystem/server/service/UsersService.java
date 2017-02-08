package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.domain.Users;
import com.musicboxsystem.server.repository.BandsRepository;
import com.musicboxsystem.server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Sibrand on 2017-01-24.
 */
@Service
public class UsersService  implements ServiceInterface <Users> {

    public UsersRepository usersRepository;

    public BandsRepository bandsRepository;

    public UsersService(UsersRepository usersRepository, BandsRepository bandsRepository) {
        this.usersRepository = usersRepository;
        this.bandsRepository = bandsRepository;
    }

    @Autowired


    @Override
    public List<Users> getObj() {
        List<Users> usersList = usersRepository.findAll();
        return convertToDTOs(usersList);
    }


    private List<Users> convertToDTOs(List<Users> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Users convertToDTO(Users users){
        Users dto = new Users();
        dto.setId(users.getId());
        dto.setName(users.getName());
        dto.setEmail(users.getEmail());
        dto.setRole(users.getRole());
        dto.setPass(users.getPass());
        dto.setLeader(users.getLeader());
        return dto;
    }

    @Override
    public Users create(Users obj)  {
        Users user = new Users(obj.getName(), obj.getEmail(), obj.getRole(),obj.getPass(),obj.getLeader());
        return usersRepository.save(user);
    }

    public boolean userExist(Users obj){
        List<Users> usersList = usersRepository.findAll();
        for (Users user: usersList) {
            if(user.getEmail().equals(obj.getEmail()))
            {
                return true;
            }
        }
        return false;
    }

    public Users findByEmail(String email) {
        List<Users> usersList = usersRepository.findAll();
        Users userReturn = new Users();

            for (Users user: usersList) {
                if(user.getEmail().equals(email))
                {
                    userReturn=user;
                    break;
                }
            }
            return userReturn;

    }

    public String bandIdIfLeader(String email){
        List<Bands> bandsList = bandsRepository.findAll();
        String bandId="";
        for(Bands band : bandsList){
            if(band.getLeader().equals(email)){
                bandId=band.getId();
            }
        }
        return bandId;
    }

    public boolean passConfirmation(String pass, String passConf){
        if(pass.equals(passConf))
            return true;
        else
            return false;
    }

    @Override
    public Users findById(String id) {
        Users users = usersRepository.findOne(id);
        return users;
    }

    @Override
    public Users update(Users obj, String id) {
        Users users = usersRepository.findOne(id);
        users.setName(obj.getName());
        users.setEmail(obj.getEmail());
        users.setRole(obj.getRole());
        users.setLeader(obj.getLeader());
        return usersRepository.save(users);
    }

    @Override
    public void delete(String obj) {
        usersRepository.delete(obj);
    }


}
