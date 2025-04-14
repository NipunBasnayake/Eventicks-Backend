package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.EventRepository;
import edu.icet.eventicks.service.EventService;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventDto == null || eventRepository.existsById(eventDto.getEventId())) {
            return null;
        }
        return modelMapper.map(eventRepository.save(modelMapper.map(eventDto, EventEntity.class)), EventDto.class);
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        if (eventId == null || !eventRepository.existsById(eventId)) {
            return null;
        }
        Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
        return eventEntity.map(entity -> modelMapper.map(entity, EventDto.class)).orElse(null);
    }

    @Override
    public List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm) {
        if ((category == null || category.isEmpty()) &&
                (venueLocation == null || venueLocation.isEmpty()) &&
                (searchTerm == null || searchTerm.isEmpty())) {
            return eventRepository.findAll().stream()
                    .map(entity -> modelMapper.map(entity, EventDto.class))
                    .toList();
        }

        List<EventEntity> allEvents = eventRepository.findAll();
        List<EventDto> filteredEvents = new ArrayList<>();

        for (EventEntity entity : allEvents) {
            boolean categoryMatch = category == null || category.isEmpty() || entity.getCategory().equals(category);
            boolean locationMatch = venueLocation == null || venueLocation.isEmpty() || entity.getVenueLocation().equals(venueLocation);
            boolean searchMatch = searchTerm == null || searchTerm.isEmpty() ||
                    entity.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    entity.getDescription().toLowerCase().contains(searchTerm.toLowerCase());

            if (categoryMatch && locationMatch && searchMatch) {
                filteredEvents.add(modelMapper.map(entity, EventDto.class));
            }
        }

        return filteredEvents;
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();

        return eventRepository.findAll().stream()
                .filter(event -> event.getEventDate().isAfter(now))
                .sorted(Comparator.comparing(EventEntity::getEventDate))
                .map(entity -> modelMapper.map(entity, EventDto.class))
                .toList();
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        if (eventId == null || eventDto == null || !eventRepository.existsById(eventId) || !eventId.equals(eventDto.getEventId())) {
            return null;
        }

        EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
        EventEntity updatedEntity = eventRepository.save(eventEntity);

        return modelMapper.map(updatedEntity, EventDto.class);
    }

    @Override
    public Boolean deleteEvent(Integer eventId) {
        if (eventId == null || !eventRepository.existsById(eventId)) {
            return false;
        }

        eventRepository.deleteById(eventId);
        return !eventRepository.existsById(eventId);
    }

    @Override
    public List<EventDto> getEventsByCreator(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        UserEntity creator = modelMapper.map(userService.getUserById(userId), UserEntity.class);
        if (creator == null) {
            return Collections.emptyList();
        }

        return eventRepository.findByCreatedBy(creator).stream()
                .map(entity -> modelMapper.map(entity, EventDto.class))
                .toList();
    }

    @Override
    public List<String> getAllCategories() {
        return eventRepository.findAll().stream()
                .map(EventEntity::getCategory)
                .distinct()
                .toList();
    }

    @Override
    public List<String> getAllLocations() {
        return eventRepository.findAll().stream()
                .map(EventEntity::getVenueLocation)
                .distinct()
                .toList();
    }
}