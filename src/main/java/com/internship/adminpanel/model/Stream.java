package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "stream")
public class Stream {
    @Id
    @Column(name = "stream_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stream_id;

    @NotNull(message = "Stream name is required")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "streamName")
    private String streamName;

    @NotNull(message = "Discipline is required")
    @ManyToOne
    @Column(name = "discip_id")
    private Discipline discipline;

    @ManyToMany
    @JoinTable(name = "task_stream", joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;
}
