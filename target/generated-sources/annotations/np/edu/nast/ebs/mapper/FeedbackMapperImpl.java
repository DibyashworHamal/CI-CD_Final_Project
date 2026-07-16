package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.FeedbackRequestDTO;
import np.edu.nast.ebs.dto.response.FeedbackResponseDTO;
import np.edu.nast.ebs.model.Booking;
import np.edu.nast.ebs.model.Event;
import np.edu.nast.ebs.model.Feedback;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T16:02:31+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Override
    public Feedback toEntity(FeedbackRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Feedback feedback = new Feedback();

        feedback.setBooking( feedbackRequestDTOToBooking( dto ) );
        feedback.setComment( dto.getComment() );
        feedback.setRating( dto.getRating() );

        return feedback;
    }

    @Override
    public FeedbackResponseDTO toDto(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }

        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();

        feedbackResponseDTO.setBookingId( feedbackBookingBookingId( feedback ) );
        feedbackResponseDTO.setEventTitle( feedbackBookingEventTitle( feedback ) );
        feedbackResponseDTO.setCustomerId( feedbackBookingCustomerUserId( feedback ) );
        feedbackResponseDTO.setComment( feedback.getComment() );
        feedbackResponseDTO.setFeedbackId( feedback.getFeedbackId() );
        feedbackResponseDTO.setRating( feedback.getRating() );
        feedbackResponseDTO.setSubmittedAt( feedback.getSubmittedAt() );

        return feedbackResponseDTO;
    }

    protected Booking feedbackRequestDTOToBooking(FeedbackRequestDTO feedbackRequestDTO) {
        if ( feedbackRequestDTO == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setBookingId( feedbackRequestDTO.getBookingId() );

        return booking;
    }

    private Integer feedbackBookingBookingId(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Booking booking = feedback.getBooking();
        if ( booking == null ) {
            return null;
        }
        Integer bookingId = booking.getBookingId();
        if ( bookingId == null ) {
            return null;
        }
        return bookingId;
    }

    private String feedbackBookingEventTitle(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Booking booking = feedback.getBooking();
        if ( booking == null ) {
            return null;
        }
        Event event = booking.getEvent();
        if ( event == null ) {
            return null;
        }
        String title = event.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private Integer feedbackBookingCustomerUserId(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Booking booking = feedback.getBooking();
        if ( booking == null ) {
            return null;
        }
        User customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Integer userId = customer.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
