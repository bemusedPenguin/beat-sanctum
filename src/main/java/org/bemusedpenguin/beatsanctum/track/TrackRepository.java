package org.bemusedpenguin.beatsanctum.track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findByEventIdOrderByUploadedAtAsc(Long eventId);

}
