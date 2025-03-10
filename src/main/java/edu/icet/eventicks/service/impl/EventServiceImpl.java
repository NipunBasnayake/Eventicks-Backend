package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.service.EventService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class EventServiceImpl implements EventService {
    @Override
    public ResponseEntity<Boolean> addEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteEvent(Long id) {
        return null;
    }
}
