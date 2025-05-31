package com.lamashkevich.lotservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "odometers")
public class Odometer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer value;

    @Enumerated(EnumType.STRING)
    private OdometerUnit unit;

    @Enumerated(EnumType.STRING)
    private OdometerStatus status;

}
