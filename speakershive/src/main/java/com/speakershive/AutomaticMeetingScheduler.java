package com.speakershive;

import com.speakershive.model.Meeting;
import com.speakershive.repo.MeetingRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Component
public class AutomaticMeetingScheduler {

    private final MeetingRepo meetingRepo;

    // 🎲 RANDOM POOLS (The robot picks from here)
    private final List<String> THEMES = List.of(
            "The Art of Storytelling", "Leadership in Crisis", "The Power of Silence",
            "Future of AI", "Mindfulness at Work", "Breaking Barriers",
            "The Joy of Missing Out", "Innovation Mindset", "Building Bridges", "Sustainable Living",
            "Courage to Lead", "Digital Minimalism", "The Infinite Game"
    );

    private final List<String> WORDS = List.of(
            "Resilience", "Eloquent", "Articulate", "Visionary", "Integrity",
            "Empathy", "Tenacity", "Serendipity", "Paradigm", "Cacophony",
            "Ephemeral", "Sonder", "Mellifluous"
    );

    public AutomaticMeetingScheduler(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    // ⏰ Runs Every Day at Midnight to check if we need meetings
    // Also runs on Startup to ensure data exists immediately
    @Scheduled(cron = "0 0 0 * * *")
    public void ensureUpcomingMeetingsExist() {
        LocalDate today = LocalDate.now();
        List<Meeting> futureMeetings = meetingRepo.findByDateGreaterThanEqualOrderByDateAsc(today);

        // If we have fewer than 3 upcoming meetings, generate the next batch!
        if (futureMeetings.size() < 3) {
            System.out.println("🤖 SCHEDULER: Generating new meetings for the upcoming weekend...");
            generateWeekendMeetings();
        }
    }

    public void generateWeekendMeetings() {
        LocalDate today = LocalDate.now();
        LocalDate nextSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        LocalDate nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        // 1. Create Saturday Online
        createMeeting("Saturday Online", nextSaturday, LocalTime.of(15, 0), LocalTime.of(16, 0));

        // 2. Create Sunday Online
        createMeeting("Sunday Online", nextSunday, LocalTime.of(8, 0), LocalTime.of(9, 0));

        // 3. Create Sunday Offline
        createMeeting("Sunday Offline", nextSunday, LocalTime.of(10, 0), LocalTime.of(11, 30));
    }

    private void createMeeting(String type, LocalDate date, LocalTime start, LocalTime end) {
        // Pick Random Theme & Word
        String theme = THEMES.get(new Random().nextInt(THEMES.size()));
        String word = WORDS.get(new Random().nextInt(WORDS.size()));

        Meeting m = new Meeting();
        m.setType(type);
        m.setDate(date);
        m.setStartTime(start);
        m.setEndTime(end);
        m.setTheme(theme);
        m.setWordOfTheDay(word);
        m.setChapterId("CH-001");

        meetingRepo.save(m);
        System.out.println("✅ CREATED: " + type + " on " + date + " [" + theme + "]");
    }
}