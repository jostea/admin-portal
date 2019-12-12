package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

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
    @Pattern(regexp = "^[a-zA-Z0-9\\s+.#]*$", message = "This name cannot be applied. Must contains only upper case,lower case, numbers and symbol '.','+','#'")
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
    @JoinTable(name = "skills_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skill;

    @ManyToMany
    @JoinTable(name = "sql_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "sql_task_id"))
    private List<SqlTask> sqlTasks;

    @ManyToMany
    @JoinTable(name = "code_stream_table", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "code_task_id"))
    private List<CodeTask> codeTasks;

    @OneToMany(mappedBy = "stream")
    private List<TestStructure> testStructures;

    @OneToOne(mappedBy = "stream")
    private StreamTime streamTime;

    @Override
    public String toString() {
        return "Stream{" +
                "name='" + name + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return name.equals(stream.name) &&
                discipline.equals(stream.discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discipline);
    }
}

