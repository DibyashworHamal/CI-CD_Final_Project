package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.OrganizerRequestDTO;
import np.edu.nast.ebs.dto.response.OrganizerRequestResponseDTO;
import np.edu.nast.ebs.model.OrganizerRequest;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T16:02:31+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class OrganizerRequestMapperImpl implements OrganizerRequestMapper {

    @Override
    public OrganizerRequest toEntity(OrganizerRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrganizerRequest organizerRequest = new OrganizerRequest();

        organizerRequest.setUser( organizerRequestDTOToUser( dto ) );
        organizerRequest.setBusinessName( dto.getBusinessName() );
        organizerRequest.setContactPhone( dto.getContactPhone() );
        organizerRequest.setTaxId( dto.getTaxId() );

        return organizerRequest;
    }

    @Override
    public OrganizerRequestResponseDTO toDto(OrganizerRequest request) {
        if ( request == null ) {
            return null;
        }

        OrganizerRequestResponseDTO organizerRequestResponseDTO = new OrganizerRequestResponseDTO();

        organizerRequestResponseDTO.setUserId( requestUserUserId( request ) );
        organizerRequestResponseDTO.setUserFullName( requestUserFullName( request ) );
        organizerRequestResponseDTO.setUserEmail( requestUserEmail( request ) );
        organizerRequestResponseDTO.setProcessedByAdminName( requestProcessedByFullName( request ) );
        organizerRequestResponseDTO.setAdminComments( request.getAdminComments() );
        if ( request.getApprovalStatus() != null ) {
            organizerRequestResponseDTO.setApprovalStatus( request.getApprovalStatus().name() );
        }
        organizerRequestResponseDTO.setApprovedAt( request.getApprovedAt() );
        organizerRequestResponseDTO.setBusinessName( request.getBusinessName() );
        organizerRequestResponseDTO.setContactPhone( request.getContactPhone() );
        organizerRequestResponseDTO.setDocumentPath( request.getDocumentPath() );
        if ( request.getPaymentStatus() != null ) {
            organizerRequestResponseDTO.setPaymentStatus( request.getPaymentStatus().name() );
        }
        organizerRequestResponseDTO.setRequestId( request.getRequestId() );
        organizerRequestResponseDTO.setRequestedAt( request.getRequestedAt() );
        organizerRequestResponseDTO.setTaxId( request.getTaxId() );

        return organizerRequestResponseDTO;
    }

    protected User organizerRequestDTOToUser(OrganizerRequestDTO organizerRequestDTO) {
        if ( organizerRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( organizerRequestDTO.getUserId() );

        return user;
    }

    private Integer requestUserUserId(OrganizerRequest organizerRequest) {
        if ( organizerRequest == null ) {
            return null;
        }
        User user = organizerRequest.getUser();
        if ( user == null ) {
            return null;
        }
        Integer userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private String requestUserFullName(OrganizerRequest organizerRequest) {
        if ( organizerRequest == null ) {
            return null;
        }
        User user = organizerRequest.getUser();
        if ( user == null ) {
            return null;
        }
        String fullName = user.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private String requestUserEmail(OrganizerRequest organizerRequest) {
        if ( organizerRequest == null ) {
            return null;
        }
        User user = organizerRequest.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String requestProcessedByFullName(OrganizerRequest organizerRequest) {
        if ( organizerRequest == null ) {
            return null;
        }
        User processedBy = organizerRequest.getProcessedBy();
        if ( processedBy == null ) {
            return null;
        }
        String fullName = processedBy.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }
}
