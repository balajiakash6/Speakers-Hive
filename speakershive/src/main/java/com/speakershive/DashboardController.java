package com.speakershive;

import com.speakershive.model.Booking;
import com.speakershive.model.Meeting;
import com.speakershive.repo.BookingRepo;
import com.speakershive.repo.MeetingRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final MeetingRepo meetingRepo;
    private final BookingRepo bookingRepo;

    // 👤 SESSION DATA
    private String currentUser = "Balaji Akash";
    private String currentRole = "MEMBER";

    // 👥 MOCK MEMBER DATABASE
    private final List<String> ALL_MEMBERS = List.of(
            "Balaji Akash", "John Doe", "Jane Smith", "Alice Johnson",
            "Bob Brown", "Charlie Davis", "David Wilson", "Emma Thomas",
            "Frank Miller", "Grace Lee", "Hannah White"
    );

    private final List<String> ROLES = List.of(
            "MC (Master of Ceremony)",
            "Hive Talk Speaker 1", "Hive Talk Speaker 2", "Hive Talk Speaker 3",
            "Peer Reviewer 1", "Peer Reviewer 2", "Peer Reviewer 3",
            "JAM Master", "Wordsmith", "Time Keeper", "Votes Counter"
    );

    public DashboardController(MeetingRepo meetingRepo, BookingRepo bookingRepo) {
        this.meetingRepo = meetingRepo;
        this.bookingRepo = bookingRepo;
    }

    // --- 📊 DASHBOARD ---
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("activeTab", "dashboard");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("allMembers", ALL_MEMBERS);

        // 🚨 CHANGED: Fetch ALL meetings (past & future) so you can see if creation worked
        List<Meeting> meetings = meetingRepo.findAll();

        // Sort them manually just in case
        meetings.sort(Comparator.comparing(Meeting::getDate));

        model.addAttribute("meetings", meetings);
        model.addAttribute("roles", ROLES);

        Map<Long, Map<String, String>> meetingBookings = new HashMap<>();
        for (Meeting m : meetings) {
            List<Booking> bookings = bookingRepo.findByMeetingId(m.getId());
            meetingBookings.put(m.getId(), bookings.stream().collect(Collectors.toMap(Booking::getRoleName, Booking::getMemberName)));
        }
        model.addAttribute("meetingBookings", meetingBookings);
        return "dashboard";
    }

    // --- 🛠 ADMIN: CREATE MEETING (FIXED TIME FORMAT) ---
    @PostMapping("/admin/create-meeting")
    public String createMeeting(
            @RequestParam("chapterId") String chapterId,
            @RequestParam("type") String type,
            // Date is standard yyyy-MM-dd
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            // 🚨 CRITICAL FIX: Explicitly accept HH:mm (HTML5 Time Input format)
            @RequestParam("start") @DateTimeFormat(pattern = "HH:mm") LocalTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "HH:mm") LocalTime end,
            @RequestParam("theme") String theme,
            @RequestParam("word") String word) {

        System.out.println("🚀 RECEIVED REQUEST: " + type + " | " + date + " | " + start + "-" + end);

        if (!"ADMIN".equals(currentRole)) {
            System.out.println("❌ Access Denied");
            return "redirect:/dashboard";
        }

        Meeting m = new Meeting();
        m.setChapterId(chapterId);
        m.setType(type);
        m.setDate(date);
        m.setStartTime(start);
        m.setEndTime(end);
        m.setTheme(theme);
        m.setWordOfTheDay(word);

        Meeting saved = meetingRepo.save(m);
        System.out.println("✅ Meeting Saved! ID: " + saved.getId());

        return "redirect:/dashboard";
    }

    // --- 🗑 ADMIN: DELETE MEETING ---
    @PostMapping("/admin/delete-meeting")
    @Transactional
    public String deleteMeeting(@RequestParam Long mid) {
        if (!"ADMIN".equals(currentRole)) return "redirect:/dashboard";
        List<Booking> bookings = bookingRepo.findByMeetingId(mid);
        bookingRepo.deleteAll(bookings);
        meetingRepo.deleteById(mid);
        return "redirect:/dashboard";
    }

    // --- 📝 BOOKING ---
    @PostMapping("/book")
    public String book(@RequestParam Long mid, @RequestParam String role,
                       @RequestParam(required = false) String assignTo) {
        String bookFor = currentUser;
        if ("ADMIN".equals(currentRole) && assignTo != null && !assignTo.isEmpty()) {
            bookFor = assignTo;
        }
        if (bookingRepo.findByMeetingIdAndRoleName(mid, role).isEmpty()) {
            bookingRepo.save(new Booking(mid, role, bookFor));
        }
        return "redirect:/dashboard";
    }

    // --- ❌ UNBOOKING ---
    @PostMapping("/unbook")
    @Transactional
    public String unbook(@RequestParam Long mid, @RequestParam String role) {
        bookingRepo.deleteByMeetingIdAndRoleName(mid, role);
        return "redirect:/dashboard";
    }

    // --- 🔄 SWITCH USER MODE ---
    @GetMapping("/switch-mode")
    public String switchMode() {
        if ("MEMBER".equals(currentRole)) {
            currentRole = "ADMIN";
            currentUser = "Admin User";
        } else {
            currentRole = "MEMBER";
            currentUser = "Balaji Akash";
        }
        return "redirect:/dashboard";
    }

    // --- 📅 AGENDA ---
    @GetMapping("/agenda")
    public String agenda(@RequestParam(required = false) Long mid, Model model) {
        model.addAttribute("activeTab", "agenda");
        List<Meeting> meetings = meetingRepo.findAllByOrderByDateAsc();
        model.addAttribute("meetings", meetings);

        Meeting selected = new Meeting();
        selected.setId(-1L); selected.setType("No Meetings"); selected.setTheme("N/A"); selected.setWordOfTheDay("N/A"); selected.setDate(LocalDate.now());

        if (!meetings.isEmpty()) {
            selected = meetings.get(0);
            if (mid != null) {
                Long fid = mid;
                selected = meetings.stream().filter(m -> m.getId().equals(fid)).findFirst().orElse(meetings.get(0));
            } else {
                Optional<Meeting> nextUp = meetings.stream().filter(m -> !m.getDate().isBefore(LocalDate.now())).findFirst();
                if(nextUp.isPresent()) selected = nextUp.get();
            }
        }
        model.addAttribute("selected", selected);

        Map<String, String> assigns = new HashMap<>();
        if (selected.getId() != -1L) {
            assigns = bookingRepo.findByMeetingId(selected.getId()).stream().collect(Collectors.toMap(Booking::getRoleName, Booking::getMemberName));
        }
        model.addAttribute("assigns", assigns);
        return "agenda";
    }

    // --- 📊 REPORTS ---
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("activeTab", "reports");
        model.addAttribute("detailedReports", generateDetailedReports());
        return "reports";
    }

    @GetMapping("/reports/download")
    public void downloadReports(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"Hive_Detailed_Reports.csv\"");
        List<MemberReport> reports = generateDetailedReports();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Member Name,Role Played,Times Played,Dates Played");
            for (MemberReport r : reports) {
                writer.println(escapeCsv(r.memberName) + "," + escapeCsv(r.roleName) + "," + r.count + "," + escapeCsv(r.dates));
            }
        }
    }

    private List<MemberReport> generateDetailedReports() {
        List<Booking> bookings = bookingRepo.findAll();
        List<Meeting> meetings = meetingRepo.findAll();
        Map<Long, LocalDate> dateMap = meetings.stream().collect(Collectors.toMap(Meeting::getId, Meeting::getDate));
        Map<String, MemberReport> reportMap = new HashMap<>();
        for (Booking b : bookings) {
            String key = b.getMemberName() + "|" + b.getRoleName();
            LocalDate date = dateMap.getOrDefault(b.getMeetingId(), LocalDate.now());
            reportMap.putIfAbsent(key, new MemberReport(b.getMemberName(), b.getRoleName()));
            reportMap.get(key).increment(date.toString());
        }
        return new ArrayList<>(reportMap.values());
    }

    private String escapeCsv(String data) {
        if (data == null) return "";
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) return "\"" + data.replace("\"", "\"\"") + "\"";
        return data;
    }

    public static class MemberReport {
        public String memberName;
        public String roleName;
        public int count;
        public String dates;
        public MemberReport(String memberName, String roleName) { this.memberName = memberName; this.roleName = roleName; this.count = 0; this.dates = ""; }
        public void increment(String date) { this.count++; if (this.dates.isEmpty()) this.dates = date; else this.dates += ", " + date; }
    }
}