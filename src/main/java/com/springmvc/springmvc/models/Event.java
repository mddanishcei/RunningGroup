package com.springmvc.springmvc.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="events")
public class Event extends BaseModel{
    
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;
    private String photoUrl;
    @UpdateTimestamp
    private LocalDateTime updatedON;

    @ManyToOne
    @JoinColumn(name="club_id",nullable = false)
    private Club club;

}
