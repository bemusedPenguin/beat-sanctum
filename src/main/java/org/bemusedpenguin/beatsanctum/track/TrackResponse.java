package org.bemusedpenguin.beatsanctum.track;

import java.time.LocalDateTime;

public record TrackResponse(Long id, Long eventId, String uploadedBy, String title,
                            String originalFilename, String mimeType, Long fileSize,
                            Integer durationSeconds, String description, LocalDateTime uploadedAt) {

    public static TrackResponse from(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getEvent().getId(),
                track.getUploadedBy().getUsername(),
                track.getTitle(),
                track.getOriginalFilename(),
                track.getMimeType(),
                track.getFileSize(),
                track.getDurationSeconds(),
                track.getDescription(),
                track.getUploadedAt()
        );
    }
}
