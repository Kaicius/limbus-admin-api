package com.kaio.limbus_admin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ego")
@Getter @Setter @NoArgsConstructor
public class Ego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sinner_id")
    private Sinner sinner;

    @Column(length = 500)
    private String artUrl;

    @Column(length = 500)
    private String obtainmentVideoUrl;
}