package org.bemusedpenguin.beatsanctum.event;

import jakarta.persistence.*;
import org.bemusedpenguin.beatsanctum.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Event() {}

    public Event(String name, String description, User createdBy, LocalDate eventDate) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.eventDate = eventDate;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
