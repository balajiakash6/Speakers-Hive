package com.speakershive.repo;

import com.speakershive.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByMeetingId(Long meetingId);
    Optional<Booking> findByMeetingIdAndRoleName(Long meetingId, String roleName);
    void deleteByMeetingIdAndRoleName(Long meetingId, String roleName);
}
