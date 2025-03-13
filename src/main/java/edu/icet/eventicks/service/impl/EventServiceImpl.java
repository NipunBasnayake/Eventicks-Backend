package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @Override
    public EventDto createEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        return null;
    }

    @Override
    public List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm) {
        return List.of();
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        return List.of();
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        return null;
    }

    @Override
    public void deleteEvent(Integer eventId) {

    }

    @Override
    public List<EventDto> getEventsByCreator(Integer userId) {
        return List.of();
    }

    @Override
    public List<String> getAllCategories() {
        return List.of();
    }

    @Override
    public List<String> getAllLocations() {
        return List.of();
    }
}
