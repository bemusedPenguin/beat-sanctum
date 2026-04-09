package org.bemusedpenguin.beatsanctum.track;

import org.bemusedpenguin.beatsanctum.config.S3Properties;
import org.bemusedpenguin.beatsanctum.event.Event;
import org.bemusedpenguin.beatsanctum.event.EventService;
import org.bemusedpenguin.beatsanctum.user.User;
import org.bemusedpenguin.beatsanctum.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class TrackService {

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    private static final List<String> ALLOWED_MIME_TYPES = List.of("audio/mpeg", "audio/wav");

    private final TrackRepository trackRepository;
    private final EventService eventService;
    private final UserService userService;
    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public TrackService(TrackRepository trackRepository, EventService eventService,
                        UserService userService, S3Client s3Client, S3Properties s3Properties) {
        this.trackRepository = trackRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.s3Client = s3Client;
        this.s3Properties = s3Properties;
    }

    public Track uploadTrack(Long eventId, String title, MultipartFile file, String username) throws IOException {
        if (file.getSize() > maxFileSize.toBytes()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 100MB limit");
        }

        String mimeType = detectMimeType(file);
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only MP3 and WAV files are allowed");
        }

        Event event = eventService.getEvent(eventId);
        User user = userService.findByUsername(username);

        String fileKey = "tracks/" + eventId + "/" + UUID.randomUUID() + "/" + file.getOriginalFilename();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(fileKey)
                        .contentType(mimeType)
                        .contentLength(file.getSize())
                        .build(),
                        RequestBody.fromBytes(file.getBytes()));

        Track track = new Track(event, user, title, file.getOriginalFilename(),
                fileKey, mimeType, file.getSize());

        return trackRepository.save(track);
    }

    public List<Track> getTracksForEvent(Long eventId) {
        eventService.getEvent(eventId); // verify event exists
        return trackRepository.findByEventIdOrderByUploadedAtAsc(eventId);
    }

    public Track getTrack(Long eventId, Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found"));

        if (!track.getEvent().getId().equals(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }

        return track;
    }

    public InputStream streamTrack(Long eventId, Long trackId) {
        Track track = getTrack(eventId, trackId);
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(track.getFileKey())
                        .build());
        return response.asInputStream();
    }

    public void deleteTrack(Long eventId, Long trackId, String username) {
        Track track = getTrack(eventId, trackId);
        User user = userService.findByUsername(username);

        if (!track.getUploadedBy().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this track");
        }

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3Properties.bucket())
                .key(track.getFileKey())
                .build());

        trackRepository.delete(track);
    }

    private String detectMimeType(MultipartFile file) throws IOException {
        byte[] header = new byte[12];
        try (InputStream is = file.getInputStream()) {
            is.read(header);
        }
        // WAV magic bytes: RIFF....WAVE
        if (header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'A' && header[10] == 'V' && header[11] == 'E') {
            return "audio/wav";
        }
        // MP3 magic bytes: ID3 or 0xFF 0xFB
        if ((header[0] == 'I' && header[1] == 'D' && header[2] == '3')
                || (header[0] == (byte) 0xFF && header[1] == (byte) 0xFB)) {
            return "audio/mpeg";
        }
        return "application/octet-stream";
    }

}
