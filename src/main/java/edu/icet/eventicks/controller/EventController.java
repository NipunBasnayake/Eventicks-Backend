package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {

    final EventService eventService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addEvent (@RequestBody EventDto eventDto) {
        return eventService.addEvent(eventDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateEvent (@RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteEvent (@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }
}
