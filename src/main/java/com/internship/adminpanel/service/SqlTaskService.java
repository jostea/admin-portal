package com.internship.adminpanel.service;

import com.internship.adminpanel.model.SqlGroup;
import com.internship.adminpanel.model.SqlTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.task.SqlTaskInsertDTO;
import com.internship.adminpanel.model.dto.task.SqlTaskListDTO;
import com.internship.adminpanel.repository.SqlGroupRepository;
import com.internship.adminpanel.repository.SqlTaskRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqlTaskService {

    private final SqlTaskRepository sqlTaskRepository;
    private final SqlGroupRepository sqlGroupRepository;
    private final StreamRepository streamRepository;

    public List<SqlTaskListDTO> getAll() {
        ArrayList<SqlTaskListDTO> tasksDto = new ArrayList<>();
        ArrayList<SqlTask> tasks;
        tasks = (ArrayList<SqlTask>) sqlTaskRepository.findAll();
        for (SqlTask task : tasks) {
            SqlTaskListDTO taskDTO = new SqlTaskListDTO(task);
            tasksDto.add(taskDTO);
        }
        return tasksDto;
    }

    @Transactional
    public void addSqlTask(SqlTaskInsertDTO sqlTaskInsertDTO) throws Exception {
        //create Task entity and save it
        SqlTask taskSql = new SqlTask(sqlTaskInsertDTO);

        //process streams
        List<Stream> listStreamsToPersist = new ArrayList<>();
        for (Long streamID : sqlTaskInsertDTO.getStreams()) {
            //get Stream object
            Stream stream;
            Optional<Stream> streamOptional = streamRepository.findById(streamID);
            if (streamOptional.isPresent()) {
                stream = streamOptional.get();
                listStreamsToPersist.add(stream);
            }
            taskSql.setStreams(listStreamsToPersist);
        }

        //process SqlGroup
        Optional<SqlGroup> sqlGroupOptional = sqlGroupRepository.findById(sqlTaskInsertDTO.getSqlGroupId());
        if (sqlGroupOptional.isPresent()) {
            taskSql.setSqlGroup(sqlGroupOptional.get());
        } else {
            log.warn("SqlGroup with ID " + sqlTaskInsertDTO.getSqlGroupId() + " was not found in DB");
            throw new Exception("There is no SqlGroup in DB with ID provided");
        }
        sqlTaskRepository.save(taskSql);
    }

}
