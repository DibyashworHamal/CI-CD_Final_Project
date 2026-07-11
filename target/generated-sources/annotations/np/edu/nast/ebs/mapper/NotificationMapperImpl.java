package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.NotificationRequestDTO;
import np.edu.nast.ebs.dto.response.NotificationResponseDTO;
import np.edu.nast.ebs.model.Notification;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:41:02+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setUser( notificationRequestDTOToUser( dto ) );
        notification.setMessage( dto.getMessage() );
        notification.setTitle( dto.getTitle() );

        return notification;
    }

    @Override
    public NotificationResponseDTO toDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO();

        notificationResponseDTO.setUserId( notificationUserUserId( notification ) );
        notificationResponseDTO.setUserName( notificationUserFullName( notification ) );
        notificationResponseDTO.setMessage( notification.getMessage() );
        notificationResponseDTO.setNotificationId( notification.getNotificationId() );
        notificationResponseDTO.setSentAt( notification.getSentAt() );
        if ( notification.getStatus() != null ) {
            notificationResponseDTO.setStatus( notification.getStatus().name() );
        }
        notificationResponseDTO.setTitle( notification.getTitle() );

        return notificationResponseDTO;
    }

    protected User notificationRequestDTOToUser(NotificationRequestDTO notificationRequestDTO) {
        if ( notificationRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( notificationRequestDTO.getUserId() );

        return user;
    }

    private Integer notificationUserUserId(Notification notification) {
        if ( notification == null ) {
            return null;
        }
        User user = notification.getUser();
        if ( user == null ) {
            return null;
        }
        Integer userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private String notificationUserFullName(Notification notification) {
        if ( notification == null ) {
            return null;
        }
        User user = notification.getUser();
        if ( user == null ) {
            return null;
        }
        String fullName = user.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }
}
