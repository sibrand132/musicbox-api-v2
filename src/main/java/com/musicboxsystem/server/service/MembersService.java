package com.musicboxsystem.server.service;

import com.musicboxsystem.server.domain.Members;
import com.musicboxsystem.server.repository.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Created by Sibrand on 2017-01-27.
 */
@Service
public class MembersService implements ServiceInterface <Members>, CustomInterfaceMembers {

    public MembersRepository membersRepository;

    @Autowired
    public MembersService(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    @Override
    public List<Members> getObj() {
        List<Members> membersList = membersRepository.findAll();
        return convertToDTOs(membersList);
    }

    private List<Members> convertToDTOs(List<Members> models){
        return models.stream().map(this::convertToDTO).collect(toList());
    }

    private Members convertToDTO(Members members){
        Members dto = new Members();
        dto.setId(members.getId());
        dto.setBandsId(members.getBandsId());
        dto.setUsersId(members.getUsersId());
        return dto;
    }

    @Override
    public Members create(Members obj) {
        return membersRepository.save(obj);
    }

    public Members createMember(String bandsId, String usersId) {
        Members member = new Members(bandsId,usersId);
        return membersRepository.save(member);
    }

    @Override
    public Members findById(String id) {
        Members members = membersRepository.findOne(id);
        return members;
    }

    @Override
    public Members update(Members obj, String id) {
        Members members = membersRepository.findOne(id);
        members.setBandsId(obj.getBandsId());
        members.setUsersId(obj.getUsersId());
        return membersRepository.save(members);
    }

    @Override
    public void delete(String obj) {
        membersRepository.delete(obj);
    }

    public void deleteAll(String id){
        List<Members> membersList = membersRepository.findByUsersId(id);
        for(Members member : membersList){
            membersRepository.delete(member);
        }
    }

    @Override
    public List<Members> findByBandsId(String id) {
        List<Members> membersList = membersRepository.findByBandsId(id);
        return  convertToDTOs(membersList);
    }

    public List<Members> findByUsersId(String id) {
        List<Members> membersList = membersRepository.findByUsersId(id);
        return  convertToDTOs(membersList);
    }
}
