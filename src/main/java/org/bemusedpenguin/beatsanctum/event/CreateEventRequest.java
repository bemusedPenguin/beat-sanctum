package org.bemusedpenguin.beatsanctum.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateEventRequest(

        @NotBlank(message = "Event name is required")
        @Size(max = 255, message = "Event name must be 255 characters or less")
        String name,

        String description,

        @NotNull(message = "Event date is required")
        LocalDate eventDate
) {}
