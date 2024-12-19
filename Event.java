public class Event {
    private int id;
    private String faculty;
    private String eventName;
    private String eventDate;
    private String eventDetails;
    private String eventStatus;

    public Event(int id, String faculty, String eventName, String eventDate, String eventDetails, String eventStatus) {
        this.id = id;
        this.faculty = faculty;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDetails = eventDetails;
        this.eventStatus = eventStatus;
    }

    // Add all getters
    public int getId() { return id; }
    public String getFaculty() { return faculty; }
    public String getEventName() { return eventName; }
    public String getEventDate() { return eventDate; }
    public String getEventDetails() { return eventDetails; }
    public String getStatus() { return eventStatus; }
}
