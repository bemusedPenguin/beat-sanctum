package org.bemusedpenguin.beatsanctum.track;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackResponse> uploadTrack(@PathVariable Long eventId,
                                                     @RequestParam String title,
                                                     @RequestParam MultipartFile file,
                                                     Authentication authentication) throws IOException {
        Track track = trackService.uploadTrack(eventId, title, file, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(TrackResponse.from(track));
    }

    @GetMapping
    public ResponseEntity<List<TrackResponse>> getTracksForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(trackService.getTracksForEvent(eventId)
                .stream().map(TrackResponse::from).toList());
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<TrackResponse> getTrack(@PathVariable Long eventId,
                                                  @PathVariable Long trackId) {
        return ResponseEntity.ok(TrackResponse.from(trackService.getTrack(eventId, trackId)));
    }

    @GetMapping("/{trackId}/stream")
    public ResponseEntity<InputStreamResource> streamTrack(@PathVariable Long eventId,
                                                           @PathVariable Long trackId) {
        Track track = trackService.getTrack(eventId, trackId);
        InputStream stream = trackService.streamTrack(eventId, trackId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(track.getMimeType()))
                .body(new InputStreamResource(stream));
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long eventId,
                                            @PathVariable Long trackId,
                                            Authentication authentication) {
        trackService.deleteTrack(eventId, trackId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
