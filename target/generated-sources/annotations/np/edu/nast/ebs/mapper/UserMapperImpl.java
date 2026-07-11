package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.UserRequestDTO;
import np.edu.nast.ebs.dto.response.UserResponseDTO;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:41:02+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );
        user.setPhoneNumber( dto.getPhoneNumber() );
        if ( dto.getRole() != null ) {
            user.setRole( Enum.valueOf( User.Role.class, dto.getRole() ) );
        }

        user.setFullName( dto.getFirstName() + " " + dto.getLastName() );

        return user;
    }

    @Override
    public UserResponseDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setPhoneNumber( user.getPhoneNumber() );
        if ( user.getRole() != null ) {
            userResponseDTO.setRole( user.getRole().name() );
        }
        userResponseDTO.setUserId( user.getUserId() );

        userResponseDTO.setFirstName( user.getFullName() != null ? user.getFullName().split(" ")[0] : "" );
        userResponseDTO.setLastName( user.getFullName() != null && user.getFullName().contains(" ") ? user.getFullName().substring(user.getFullName().indexOf(" ") + 1) : "" );

        return userResponseDTO;
    }
}
