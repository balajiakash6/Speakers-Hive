package com.speakershive;

import com.speakershive.repo.BookingRepo;
import com.speakershive.repo.MeetingRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MeetingRepo meetingRepo;
    private final BookingRepo bookingRepo;
    private final AutomaticMeetingScheduler scheduler;

    public DataSeeder(MeetingRepo meetingRepo, BookingRepo bookingRepo, AutomaticMeetingScheduler scheduler) {
        this.meetingRepo = meetingRepo;
        this.bookingRepo = bookingRepo;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args) {
        // Clean start for demo purposes (Optional: remove these 2 lines if you want persistent history forever)
        bookingRepo.deleteAll();
        meetingRepo.deleteAll();

        // Let the Robot generate the first batch!
        System.out.println("🌱 SEEDER: Calling Scheduler to generate initial data...");
        scheduler.generateWeekendMeetings();
    }
}
