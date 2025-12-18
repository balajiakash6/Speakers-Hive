package com.speakershive.model;

import jakarta.persistence.*;

@Entity
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long meetingId;
    private String roleName;
    private String memberName;

    public Booking() {}
    public Booking(Long meetingId, String roleName, String memberName) {
        this.meetingId = meetingId;
        this.roleName = roleName;
        this.memberName = memberName;
    }

    public Long getId() { return id; }
    public Long getMeetingId() { return meetingId; }
    public String getRoleName() { return roleName; }
    public String getMemberName() { return memberName; }
}
