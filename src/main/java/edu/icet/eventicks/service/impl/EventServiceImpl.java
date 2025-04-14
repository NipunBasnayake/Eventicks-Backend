package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.repository.EventRepository;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.EventService;

import java.time.LocalDate;
import java.util.*;

import edu.icet.eventicks.util.enums.EventCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventDto == null || eventDto.getEventId() != null) {
            return null;
        }
        return modelMapper.map(eventRepository.save(modelMapper.map(eventDto, EventEntity.class)), EventDto.class);
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        if (eventId == null || !eventRepository.existsById(eventId)) {
            return null;
        }
        return modelMapper.map(eventRepository.findById(eventId), EventDto.class);
    }

    @Override
    public List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm) {
        if ((searchTerm == null || searchTerm.isEmpty()) &&
                (venueLocation == null || venueLocation.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return Collections.emptyList();
        }

        return eventRepository.findAll().stream()
                .filter(eventEntity -> {
                    boolean matchesCategory = category == null || category.isEmpty() ||
                            (eventEntity.getCategory() != null && eventEntity.getCategory().equalsIgnoreCase(category));

                    boolean matchesVenue = venueLocation == null || venueLocation.isEmpty() ||
                            (eventEntity.getVenueLocation() != null && eventEntity.getVenueLocation().equalsIgnoreCase(venueLocation));

                    boolean matchesSearchTerm = searchTerm == null || searchTerm.isEmpty() ||
                            eventEntity.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            (eventEntity.getDescription() != null && eventEntity.getDescription().toLowerCase().contains(searchTerm.toLowerCase()));

                    return matchesCategory && matchesVenue && matchesSearchTerm;
                })
                .map(eventEntity -> modelMapper.map(eventEntity, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekFromNow = today.plusDays(7);

        return eventRepository.findAll().stream()
                .filter(eventEntity -> {
                    LocalDate eventDate = LocalDate.from(eventEntity.getEventDate());
                    return eventDate.isEqual(today) || eventDate.isAfter(today) && eventDate.isBefore(oneWeekFromNow) || eventDate.isEqual(oneWeekFromNow);
                })
                .map(eventEntity -> modelMapper.map(eventEntity, EventDto.class))
                .toList();
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        if (eventId == null || !eventRepository.existsById(eventId) && eventDto == null) {
            return null;
        }
        eventDto.setEventId(eventId);
        return modelMapper.map(eventRepository.save(modelMapper.map(eventDto, EventEntity.class)), EventDto.class);
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
        if (userId == null || !userRepository.existsById(userId)) {
            return Collections.emptyList();
        }
        return eventRepository.findByCreatedBy(userRepository.findById(userId)).stream()
                .map(eventEntity -> modelMapper.map(eventEntity, EventDto.class))
                .toList();
    }

    @Override
    public List<String> getAllCategories() {
        return Arrays.stream(EventCategory.values())
                .map(EventCategory::name)
                .toList();
    }

    @Override
    public List<String> getAllLocations() {
        return eventRepository.findAll().stream()
                .map(EventEntity::getVenueLocation)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
