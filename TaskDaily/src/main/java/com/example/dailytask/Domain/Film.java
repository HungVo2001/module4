package com.example.dailytask.Domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String description;
    private LocalDate publish;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Task task;


}
