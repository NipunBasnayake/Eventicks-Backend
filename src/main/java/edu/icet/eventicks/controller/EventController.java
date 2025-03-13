package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<EventDto>> createEvent(@RequestBody EventDto eventDto) {
        EventDto createdEvent = eventService.createEvent(eventDto);
        return new ResponseEntity<>(ApiResponseDto.success("Event created successfully", createdEvent), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<EventDto>> getEventById(@PathVariable Integer eventId) {
        EventDto event = eventService.getEventById(eventId);
        return ResponseEntity.ok(ApiResponseDto.success(event));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String venueLocation,
            @RequestParam(required = false) String searchTerm) {
        List<EventDto> events = eventService.getFilteredEvents(category, venueLocation, searchTerm);
        return ResponseEntity.ok(ApiResponseDto.success(events));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getUpcomingEvents() {
        List<EventDto> upcomingEvents = eventService.getUpcomingEvents();
        return ResponseEntity.ok(ApiResponseDto.success(upcomingEvents));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<EventDto>> updateEvent(
            @PathVariable Integer eventId,
            @RequestBody EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(eventId, eventDto);
        return ResponseEntity.ok(ApiResponseDto.success("Event updated successfully", updatedEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponseDto.success("Event deleted successfully", null));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getEventsByCreator(@PathVariable Integer userId) {
        List<EventDto> events = eventService.getEventsByCreator(userId);
        return ResponseEntity.ok(ApiResponseDto.success(events));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllCategories() {
        List<String> categories = eventService.getAllCategories();
        return ResponseEntity.ok(ApiResponseDto.success(categories));
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllLocations() {
        List<String> locations = eventService.getAllLocations();
        return ResponseEntity.ok(ApiResponseDto.success(locations));
    }
}