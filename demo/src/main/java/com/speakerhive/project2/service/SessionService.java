package com.speakerhive.project2.service;

import com.speakerhive.project2.model.Role;
import com.speakerhive.project2.model.Session;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d MMM uuuu");
    private final List<Session> sessions = new ArrayList<>();

    public SessionService() {
        List<String> roleNames = Arrays.asList(
                "MC (Master of Ceremony)",
                "Hive Talk Speaker 1",
                "Hive Talk Speaker 2",
                "Hive Talk Speaker 3",
                "JAM Master",
                "Peer Reviewer 1",
                "Peer Reviewer 2",
                "Peer Reviewer 3",
                "Wordsmith",
                "Timekeeper",
                "Votes Counter"
        );

        List<Role> proto = roleNames.stream().map(Role::new).collect(Collectors.toList());
        LocalDate nextSat = nextOrSame(DayOfWeek.SATURDAY);
        LocalDate nextSun = nextOrSame(DayOfWeek.SUNDAY);

        sessions.add(new Session("Saturday Online", nextSat.format(DATE_FMT), "3:00 PM - 4:00 PM",
                "Leadership Excellence", "Resilience", deepCloneRoles(proto)));

        sessions.add(new Session("Sunday Online", nextSun.format(DATE_FMT), "8:00 AM - 9:00 AM",
                "Communication Mastery", "Articulate", deepCloneRoles(proto)));

        sessions.add(new Session("Sunday Offline", nextSun.format(DATE_FMT), "10:00 AM - 11:30 AM",
                "Public Speaking", "Eloquent", deepCloneRoles(proto)));
    }

    public synchronized List<Session> getSessions() {
        return sessions.stream().map(this::shallowCopySession).collect(Collectors.toList());
    }

    public synchronized String assignRole(int sessionIndex, int roleIndex, String userName) {
        if (userName == null || userName.isBlank()) return null;
        if (sessionIndex < 0 || sessionIndex >= sessions.size()) return null;
        Session s = sessions.get(sessionIndex);
        if (roleIndex < 0 || roleIndex >= s.getRoles().size()) return null;
        Role r = s.getRoles().get(roleIndex);
        if (r.isAssigned()) return null;
        r.setAssignedTo(userName);
        return userName;
    }

    public synchronized boolean unassignRole(int sessionIndex, int roleIndex, String requester) {
        if (requester == null || requester.isBlank()) return false;
        if (sessionIndex < 0 || sessionIndex >= sessions.size()) return false;
        Session s = sessions.get(sessionIndex);
        if (roleIndex < 0 || roleIndex >= s.getRoles().size()) return false;
        Role r = s.getRoles().get(roleIndex);
        if (!r.isAssigned()) return false;
        if (!Objects.equals(r.getAssignedTo(), requester)) return false;
        r.setAssignedTo(null);
        return true;
    }

    private LocalDate nextOrSame(DayOfWeek dow) {
        LocalDate today = LocalDate.now();
        return today.with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
    }

    private List<Role> deepCloneRoles(List<Role> roles) {
        return roles.stream().map(r -> new Role(r.getName())).collect(Collectors.toList());
    }

    private Session shallowCopySession(Session orig) {
        List<Role> rolesCopy = orig.getRoles().stream()
                .map(r -> new Role(r.getName(), r.getAssignedTo()))
                .collect(Collectors.toList());
        return new Session(orig.getTitle(), orig.getDate(), orig.getTime(), orig.getTheme(), orig.getWordOfDay(), rolesCopy);
    }
}
