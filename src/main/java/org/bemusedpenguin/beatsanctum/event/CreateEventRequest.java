package org.bemusedpenguin.beatsanctum.event;

import java.time.LocalDate;

public record CreateEventRequest(String name, String description, LocalDate eventDate) {
}
