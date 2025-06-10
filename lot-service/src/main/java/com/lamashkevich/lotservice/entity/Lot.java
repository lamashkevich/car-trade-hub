package com.lamashkevich.lotservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lots")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer lotNumber;
    @Enumerated(EnumType.STRING)
    private AuctionType auction;
    @Enumerated(EnumType.STRING)
    private LotType type;
    private String make;
    private String model;
    private Integer year;
    private String vin;
    @OneToOne(cascade = CascadeType.ALL)
    private Odometer odometer;
    private String engine;
    @Enumerated(EnumType.STRING)
    private FuelType fuel;
    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;
    @Enumerated(EnumType.STRING)
    private DriveType drive;
    private Boolean key;
    private String damage;
    private String title;
    private LocalDateTime auctionDate;
//    private List<String> imageUrls;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
