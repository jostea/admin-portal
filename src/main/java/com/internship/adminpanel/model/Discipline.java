package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discipline")
public class Discipline {
    @Id
    @Column(name = "discipline_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long discipline_id;

    @NotNull(message = "Discipline is required")
    @Column(name = "disc_name")
    private String discipline;

    @OneToMany
    private List<Stream> streams;
}
