package com.speakershive.repo;

import com.speakershive.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MeetingRepo extends JpaRepository<Meeting, Long> {
    // Finds all meetings, sorted by date
    List<Meeting> findAllByOrderByDateAsc();

    // Finds only FUTURE meetings (for the Dashboard)
    List<Meeting> findByDateGreaterThanEqualOrderByDateAsc(LocalDate date);
}
