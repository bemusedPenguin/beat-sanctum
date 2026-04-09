package org.bemusedpenguin.beatsanctum.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EventResponse(Long id, String name, String description,
                            String createdBy, LocalDate eventDate, LocalDateTime createdAt) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCreatedBy().getUsername(),
                event.getEventDate(),
                event.getCreatedAt()
        );
    }
}
