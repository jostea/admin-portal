package com.internship.adminpanel.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotBlank(message = "Discipline name is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "This name cannot be applied. Must contains only upper case,lower case and numbers.")
    private String name;

    @OneToMany(mappedBy = "discipline")
    private List<Stream> streams;

    @Override
    public String toString() {
        return "Discipline{" +
                "name=" + name;
    }
}
