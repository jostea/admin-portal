package com.internship.adminpanel.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "stream_table")
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Stream name is required")
    private String name;

    @NotNull(message = "Discipline is required")
    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToMany
    @JoinTable(name = "task_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "sql_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "sql_task_id"))
    private List<SqlTask> sqlTasks;

    @ManyToMany
    @JoinTable(name = "code_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "code_task_id"))
    private List<CodeTask> codeTasks;

    @OneToOne(mappedBy = "stream")
    private StreamTime streamTime;

    @Override
    public String toString() {
        return "Stream{" +
                "name='" + name + '\'';
    }
}
