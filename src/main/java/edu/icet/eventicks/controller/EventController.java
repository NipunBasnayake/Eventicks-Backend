package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<EventDto>> createEvent(@RequestBody EventDto eventDto) {
        EventDto createdEvent = eventService.createEvent(eventDto);
        if (createdEvent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("Failed to create event"));
        }
        return new ResponseEntity<>(ApiResponseDto.success("Event created successfully", createdEvent), HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getAllEvents(@RequestBody List<EventDto> eventList) {
        List<EventDto> savedEvents = new ArrayList<>();

        eventList.forEach(eventDto -> {
            EventDto event = eventService.createEvent(eventDto);
            savedEvents.add(event);
        });

        ApiResponseDto<List<EventDto>> response = new ApiResponseDto<>(true, "Events created successfully", savedEvents);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<EventDto>> getEventById(@PathVariable Integer eventId) {
        EventDto event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Event not found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(event));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String venueLocation,
            @RequestParam(required = false) String searchTerm) {
        List<EventDto> events = eventService.getFilteredEvents(category, venueLocation, searchTerm);
        if (events == null || events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDto.error("No events found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(events));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getUpcomingEvents() {
        List<EventDto> upcomingEvents = eventService.getUpcomingEvents();
        if (upcomingEvents == null || upcomingEvents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDto.error("No upcoming events found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(upcomingEvents));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<EventDto>> updateEvent(
            @PathVariable Integer eventId,
            @RequestBody EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(eventId, eventDto);
        if (updatedEvent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("Failed to update event"));
        }
        return ResponseEntity.ok(ApiResponseDto.success("Event updated successfully", updatedEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteEvent(@PathVariable Integer eventId) {
        boolean isDeleted = eventService.deleteEvent(eventId);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Event not found or deletion failed"));
        }
        return ResponseEntity.ok(ApiResponseDto.success("Event deleted successfully", null));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<ApiResponseDto<List<EventDto>>> getEventsByCreator(@PathVariable Integer userId) {
        List<EventDto> events = eventService.getEventsByCreator(userId);
        if (events == null || events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDto.error("No events found for the creator"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(events));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllCategories() {
        List<String> categories = eventService.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDto.error("No categories found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(categories));
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllLocations() {
        List<String> locations = eventService.getAllLocations();
        if (locations == null || locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDto.error("No locations found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(locations));
    }
}
