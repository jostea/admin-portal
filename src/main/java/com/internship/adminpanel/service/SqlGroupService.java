package com.internship.adminpanel.service;

import com.internship.adminpanel.model.SqlGroup;
import com.internship.adminpanel.model.dto.task.SqlGroupDTO;
import com.internship.adminpanel.repository.SqlGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqlGroupService {

    private final SqlGroupRepository sqlGroupRepository;

    public List<SqlGroupDTO> getAll(){
        ArrayList<SqlGroupDTO> sqlGroupDTOs = new ArrayList<>();
        ArrayList<SqlGroup> sqlGroups;
        sqlGroups = (ArrayList<SqlGroup>) sqlGroupRepository.findAll();
        for (SqlGroup sqlGroup : sqlGroups) {
            SqlGroupDTO sqlGroupDTO = new SqlGroupDTO();
            sqlGroupDTO.setId(sqlGroup.getId());
            sqlGroupDTO.setName(sqlGroup.getName());
            sqlGroupDTO.setImagePath(sqlGroup.getImagePath());
            sqlGroupDTOs.add(sqlGroupDTO);
        }
        return sqlGroupDTOs;
    }

    public SqlGroupDTO findById(Long id) {
        Optional<SqlGroup> sqlGroupDB = sqlGroupRepository.findById(id);
        if (sqlGroupDB.isPresent())
            return new SqlGroupDTO(sqlGroupDB.get());
        else return new SqlGroupDTO();
    }
}
