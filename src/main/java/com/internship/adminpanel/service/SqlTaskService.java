package com.internship.adminpanel.service;

import com.internship.adminpanel.model.SqlGroup;
import com.internship.adminpanel.model.SqlTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.task.*;
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

    public SqlTaskEditDTO findById(Long id) {
        Optional<SqlTask> taskDB = sqlTaskRepository.findById(id);
        if (taskDB.isPresent())
            return new SqlTaskEditDTO(taskDB.get());
        else return new SqlTaskEditDTO();
    }

    @Transactional
    public void editTask(SqlTaskEditDTO sqlTaskEditDTO) throws Exception {
        SqlTask sqlTaskToEdit = new SqlTask(sqlTaskEditDTO);

        //set new collection of Streams
        sqlTaskToEdit = setStreamsToSqlTask(sqlTaskToEdit, sqlTaskEditDTO.getStreams());

        //set SqlGroup if it's id exists
        sqlTaskToEdit = setSqlGroupToSqlTask(sqlTaskEditDTO.getSqlGroupDTO().getId(), sqlTaskToEdit);

        //save edited SQL Task object
        try {
            sqlTaskRepository.save(sqlTaskToEdit);
        } catch (Exception e) {
            log.warn("Error while updating SQL Task with id " + sqlTaskEditDTO.getId());
            throw new Exception("Error while updating SQL Task with title [" + sqlTaskEditDTO.getTitle());
        }
    }

    public void disableTask(SqlTaskDisableDTO sqlTaskDisableDTO){
        //Get Task from DB to disable_enable
        SqlTask taskToEdit = sqlTaskRepository.getOne(sqlTaskDisableDTO.getId());
        taskToEdit.setEnabled(sqlTaskDisableDTO.isEnabled());
        sqlTaskRepository.save(taskToEdit);
    }


    //region PRIVATE METHODS-HELPERS

    //set SQL_GROUP to a SQL_TASK
    private SqlTask setSqlGroupToSqlTask(Long idGroup, SqlTask sqlTask) throws Exception {
        Optional<SqlGroup> sqlGroupOptional = sqlGroupRepository.findById(idGroup);
        SqlGroup sqlGroupToApply;
        if (sqlGroupOptional.isPresent()) {
            sqlGroupToApply = sqlGroupOptional.get();
            sqlTask.setSqlGroup(sqlGroupToApply);
            return sqlTask;
        } else {
            log.warn("SQL Group with id [" + idGroup + "] was not found.");
            throw new Exception("SQL Task with the title [" + idGroup + "] was not found in DB");
        }
    }

    //set Stream to a SQL_TASK
    private SqlTask setStreamsToSqlTask(SqlTask sqlTask, List<StreamDTO> streamsDTO) {
        List<Stream> listNewStreams = new ArrayList<>();
        for (StreamDTO streamDto : streamsDTO) {
            Optional<Stream> streamOptional = streamRepository.findById(streamDto.getId());
            if (streamOptional.isPresent()) {
                Stream streamDB = streamOptional.get();
                listNewStreams.add(streamDB);
            }
        }
        sqlTask.setStreams(listNewStreams);
        return sqlTask;
    }

    //endregion

}
