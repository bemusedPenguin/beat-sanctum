package org.bemusedpenguin.beatsanctum.track;

import jakarta.persistence.*;
import org.bemusedpenguin.beatsanctum.event.Event;
import org.bemusedpenguin.beatsanctum.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String fileKey;

    @Column(nullable = false, length = 50)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSize;

    private Integer durationSeconds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    protected Track() {}

    public Track(Event event, User uploadedBy, String title, String originalFilename,
                 String fileKey, String mimeType, Long fileSize) {
        this.event = event;
        this.uploadedBy = uploadedBy;
        this.title = title;
        this.originalFilename = originalFilename;
        this.fileKey = fileKey;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public User getUploadedBy() {
        return uploadedBy;

    }
    public String getTitle() {
        return title;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getFileKey() {
        return fileKey;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

}
