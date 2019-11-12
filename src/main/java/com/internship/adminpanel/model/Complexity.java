package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "complexity")
public class Complexity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "compl_id")
    private Long complexity_id;

    @NotNull(message = "The level of complexity is required")
    @Column(name = "complName")
    @Enumerated(value = EnumType.STRING)
    private ComplexityEnum complexityEnum;

    @OneToMany
    private List<Task> tasks;
}
