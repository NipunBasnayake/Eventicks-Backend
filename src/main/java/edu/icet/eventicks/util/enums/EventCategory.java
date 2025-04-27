package edu.icet.eventicks.util.enums;

public enum EventCategory {
    LIVE_CONCERTS("Live music performances by bands or solo artists"),
    DJ_NIGHTS("Electronic dance music events with DJs"),
    CLASSICAL_MUSIC("Orchestral, instrumental, and traditional classical performances"),
    OPEN_MIC("Open stage performances for musicians and singers"),
    TRIBUTE_SHOWS("Concerts honoring famous artists or bands"),
    MUSIC_FESTIVALS("Large multi-artist, multi-day live music festivals");

    private final String description;

    EventCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
