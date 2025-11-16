package com.sproutcone.domain;

import jakarta.persistence.*;

@Entity
public class RunningArt {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gpx;

    @ManyToOne
    @JoinColumn(name = "runner_id", nullable = false)
    private Runner runner;
}