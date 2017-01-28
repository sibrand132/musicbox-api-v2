package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Users;
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

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

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
        return dto;
    }

    @Override
    public Users create(Users obj) {
        return usersRepository.save(obj);
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
        return usersRepository.save(users);
    }

    @Override
    public void delete(String obj) {
        usersRepository.delete(obj);
    }


}
