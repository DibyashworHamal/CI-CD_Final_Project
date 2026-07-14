package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.UserProfileRequestDTO;
import np.edu.nast.ebs.dto.response.UserProfileResponseDTO;
import np.edu.nast.ebs.model.User;
import np.edu.nast.ebs.model.UserProfile;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T08:53:04+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfile toEntity(UserProfileRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setUser( userProfileRequestDTOToUser( dto ) );
        userProfile.setBio( dto.getBio() );
        userProfile.setLanguage( dto.getLanguage() );
        userProfile.setNotificationEmail( dto.getNotificationEmail() );
        userProfile.setPhoneNumber( dto.getPhoneNumber() );
        userProfile.setProfilePicture( dto.getProfilePicture() );
        if ( dto.getTheme() != null ) {
            userProfile.setTheme( Enum.valueOf( UserProfile.Theme.class, dto.getTheme() ) );
        }

        return userProfile;
    }

    @Override
    public UserProfileResponseDTO toDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();

        userProfileResponseDTO.setUserId( userProfileUserUserId( userProfile ) );
        userProfileResponseDTO.setBio( userProfile.getBio() );
        userProfileResponseDTO.setLanguage( userProfile.getLanguage() );
        userProfileResponseDTO.setNotificationEmail( userProfile.getNotificationEmail() );
        userProfileResponseDTO.setPhoneNumber( userProfile.getPhoneNumber() );
        userProfileResponseDTO.setProfilePicture( userProfile.getProfilePicture() );
        userProfileResponseDTO.setSettingId( userProfile.getSettingId() );
        if ( userProfile.getTheme() != null ) {
            userProfileResponseDTO.setTheme( userProfile.getTheme().name() );
        }

        return userProfileResponseDTO;
    }

    protected User userProfileRequestDTOToUser(UserProfileRequestDTO userProfileRequestDTO) {
        if ( userProfileRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( userProfileRequestDTO.getUserId() );

        return user;
    }

    private Integer userProfileUserUserId(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        Integer userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
