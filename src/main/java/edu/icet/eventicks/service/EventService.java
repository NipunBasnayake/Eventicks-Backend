package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.EventDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {

    ResponseEntity<Boolean> addEvent(EventDto eventDto);

    ResponseEntity<List<EventDto>> getAllEvents();

    ResponseEntity<Boolean> updateEvent(EventDto eventDto);

    ResponseEntity<Boolean> deleteEvent(Long id);
}
