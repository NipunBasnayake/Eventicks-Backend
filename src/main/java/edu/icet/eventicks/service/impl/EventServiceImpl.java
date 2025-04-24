package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.repository.EventRepository;
import edu.icet.eventicks.service.EventService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventDto == null) {
            return null;
        }
        if (eventDto.getEventId() == null || !eventRepository.existsById(eventDto.getEventId())) {
            EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
            eventEntity = eventRepository.save(eventEntity);
            return modelMapper.map(eventEntity, EventDto.class);
        }
        return null;
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        if (eventId != null && eventRepository.existsById(eventId)) {
            EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
            return modelMapper.map(eventEntity, EventDto.class);
        }
        return null;
    }

    @Override
    public List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm) {
        List<EventEntity> eventEntities = eventRepository.findAll();
        return eventEntities.stream()
                .filter(event -> (category == null || event.getCategory().equalsIgnoreCase(category)) &&
                        (venueLocation == null || event.getVenueLocation().equalsIgnoreCase(venueLocation)) &&
                        (searchTerm == null || event.getName().contains(searchTerm)))
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        LocalDate currentDate = LocalDate.now();
        List<EventEntity> upcomingEvents = eventRepository.findAll().stream()
                .filter(event -> event.getEventDate().isAfter(currentDate.atStartOfDay()))
                .toList();

        return upcomingEvents.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        if (eventId != null && eventRepository.existsById(eventId) && eventId.equals(eventDto.getEventId())) {
            EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
            return modelMapper.map(eventRepository.save(eventEntity), EventDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteEvent(Integer eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    @Override
    public List<EventDto> getEventsByCreator(Integer userId) {
        List<EventEntity> eventEntities = eventRepository.findAll();
        return eventEntities.stream()
                .filter(event -> userId.equals(event.getCreatedById()))
                .map(event -> modelMapper.map(event, EventDto.class))
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
