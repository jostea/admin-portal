package com.internship.adminpanel.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discipline_table")
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Discipline name is required")
    private String name;

    @OneToMany(mappedBy = "discipline")
    private List<Stream> streams;

    @Override
    public String toString() {
        return "Discipline{" +
                "name=" + name;
    }
}
