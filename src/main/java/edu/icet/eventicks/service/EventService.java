package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto);

    EventDto getEventById(Integer eventId);

    List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm);

    List<EventDto> getUpcomingEvents();

    EventDto updateEvent(Integer eventId, EventDto eventDto);

    void deleteEvent(Integer eventId);

    List<EventDto> getEventsByCreator(Integer userId);

    List<String> getAllCategories();

    List<String> getAllLocations();
}
