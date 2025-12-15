package com.speakerhive.project2.model;

public class Role {
    private String name;
    private String assignedTo;

    public Role() {}

    public Role(String name) { this.name = name; }

    public Role(String name, String assignedTo) {
        this.name = name;
        this.assignedTo = assignedTo;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public boolean isAssigned() { return assignedTo != null && !assignedTo.isBlank(); }
}
