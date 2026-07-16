package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.EventPhotoRequestDTO;
import np.edu.nast.ebs.dto.response.EventPhotoResponseDTO;
import np.edu.nast.ebs.model.Event;
import np.edu.nast.ebs.model.EventPhoto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T16:02:31+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class EventPhotoMapperImpl implements EventPhotoMapper {

    @Override
    public EventPhoto toEntity(EventPhotoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EventPhoto eventPhoto = new EventPhoto();

        eventPhoto.setEvent( eventPhotoRequestDTOToEvent( dto ) );
        eventPhoto.setPhotoUrl( dto.getPhotoUrl() );

        return eventPhoto;
    }

    @Override
    public EventPhotoResponseDTO toDto(EventPhoto eventPhoto) {
        if ( eventPhoto == null ) {
            return null;
        }

        EventPhotoResponseDTO eventPhotoResponseDTO = new EventPhotoResponseDTO();

        eventPhotoResponseDTO.setEventId( eventPhotoEventEventId( eventPhoto ) );
        eventPhotoResponseDTO.setPhotoId( eventPhoto.getPhotoId() );
        eventPhotoResponseDTO.setPhotoUrl( eventPhoto.getPhotoUrl() );

        return eventPhotoResponseDTO;
    }

    protected Event eventPhotoRequestDTOToEvent(EventPhotoRequestDTO eventPhotoRequestDTO) {
        if ( eventPhotoRequestDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setEventId( eventPhotoRequestDTO.getEventId() );

        return event;
    }

    private Integer eventPhotoEventEventId(EventPhoto eventPhoto) {
        if ( eventPhoto == null ) {
            return null;
        }
        Event event = eventPhoto.getEvent();
        if ( event == null ) {
            return null;
        }
        Integer eventId = event.getEventId();
        if ( eventId == null ) {
            return null;
        }
        return eventId;
    }
}
