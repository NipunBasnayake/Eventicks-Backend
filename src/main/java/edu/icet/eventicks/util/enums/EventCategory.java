package edu.icet.eventicks.util.enums;

public enum EventCategory {
    MUSIC_CONCERTS("Live Concerts, DJ Nights, Classical Music, Open Mic, Tribute Shows"),
    FESTIVALS_CULTURAL("Traditional Festivals, Food Festivals, Art Exhibitions, Fashion Shows"),
    THEATER_PERFORMING_ARTS("Drama, Stand-up Comedy, Dance Performances, Circus, Magic Shows"),
    SPORTS_ADVENTURE("Cricket, Football, Marathon, Motor Racing, Water Sports, Extreme Sports"),
    WORKSHOPS_CONFERENCES("Business, Tech, Photography, Music, Dance Workshops"),
    NIGHTLIFE_SOCIAL("Clubbing, Social Mixers, Wine Tasting"),
    CHARITY_COMMUNITY("Fundraising, Blood Donation, Environmental Events");

    private final String description;

    EventCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}