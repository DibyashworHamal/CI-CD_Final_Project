package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.EventRequestDTO;
import np.edu.nast.ebs.dto.response.EventResponseDTO;
import np.edu.nast.ebs.model.Category;
import np.edu.nast.ebs.model.Event;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:41:02+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public Event toEntity(EventRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Event event = new Event();

        event.setOrganizer( eventRequestDTOToUser( dto ) );
        event.setCategory( eventRequestDTOToCategory( dto ) );
        event.setDescription( dto.getDescription() );
        event.setLocation( dto.getLocation() );
        event.setPrice( dto.getPrice() );
        event.setStartDateTime( dto.getStartDateTime() );
        event.setTitle( dto.getTitle() );

        return event;
    }

    @Override
    public EventResponseDTO toDto(Event event) {
        if ( event == null ) {
            return null;
        }

        EventResponseDTO eventResponseDTO = new EventResponseDTO();

        eventResponseDTO.setOrganizerId( eventOrganizerUserId( event ) );
        eventResponseDTO.setCategoryId( eventCategoryCategoryId( event ) );
        eventResponseDTO.setOrganizerName( eventOrganizerFullName( event ) );
        eventResponseDTO.setCategoryName( eventCategoryName( event ) );
        eventResponseDTO.setStartDateTime( event.getStartDateTime() );
        eventResponseDTO.setEndDateTime( event.getEndDateTime() );
        eventResponseDTO.setCoverImagePath( event.getCoverImagePath() );
        eventResponseDTO.setCustomCategory( event.getCustomCategory() );
        eventResponseDTO.setTotalTickets( event.getTotalTickets() );
        eventResponseDTO.setOrganizerContact( event.getOrganizerContact() );
        eventResponseDTO.setEventWebsite( event.getEventWebsite() );
        eventResponseDTO.setFeatured( event.isFeatured() );
        eventResponseDTO.setRegistrationRequired( event.isRegistrationRequired() );
        eventResponseDTO.setCreatedAt( event.getCreatedAt() );
        eventResponseDTO.setDescription( event.getDescription() );
        eventResponseDTO.setEventId( event.getEventId() );
        eventResponseDTO.setLocation( event.getLocation() );
        eventResponseDTO.setPrice( event.getPrice() );
        eventResponseDTO.setTitle( event.getTitle() );

        return eventResponseDTO;
    }

    protected User eventRequestDTOToUser(EventRequestDTO eventRequestDTO) {
        if ( eventRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( eventRequestDTO.getOrganizerId() );

        return user;
    }

    protected Category eventRequestDTOToCategory(EventRequestDTO eventRequestDTO) {
        if ( eventRequestDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setCategoryId( eventRequestDTO.getCategoryId() );

        return category;
    }

    private Integer eventOrganizerUserId(Event event) {
        if ( event == null ) {
            return null;
        }
        User organizer = event.getOrganizer();
        if ( organizer == null ) {
            return null;
        }
        Integer userId = organizer.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Integer eventCategoryCategoryId(Event event) {
        if ( event == null ) {
            return null;
        }
        Category category = event.getCategory();
        if ( category == null ) {
            return null;
        }
        Integer categoryId = category.getCategoryId();
        if ( categoryId == null ) {
            return null;
        }
        return categoryId;
    }

    private String eventOrganizerFullName(Event event) {
        if ( event == null ) {
            return null;
        }
        User organizer = event.getOrganizer();
        if ( organizer == null ) {
            return null;
        }
        String fullName = organizer.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private String eventCategoryName(Event event) {
        if ( event == null ) {
            return null;
        }
        Category category = event.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
