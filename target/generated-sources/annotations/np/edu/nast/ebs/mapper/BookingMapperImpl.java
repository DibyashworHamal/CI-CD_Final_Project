package np.edu.nast.ebs.mapper;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.BookingRequestDTO;
import np.edu.nast.ebs.dto.response.BookingResponseDTO;
import np.edu.nast.ebs.model.Booking;
import np.edu.nast.ebs.model.Event;
import np.edu.nast.ebs.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T16:02:31+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking toEntity(BookingRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setEvent( bookingRequestDTOToEvent( dto ) );
        booking.setCustomer( bookingRequestDTOToUser( dto ) );

        return booking;
    }

    @Override
    public BookingResponseDTO toDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setEventId( bookingEventEventId( booking ) );
        bookingResponseDTO.setEventTitle( bookingEventTitle( booking ) );
        bookingResponseDTO.setEventPrice( bookingEventPrice( booking ) );
        bookingResponseDTO.setCustomerId( bookingCustomerUserId( booking ) );
        bookingResponseDTO.setCustomerName( bookingCustomerFullName( booking ) );
        bookingResponseDTO.setBookingDate( booking.getBookingDate() );
        bookingResponseDTO.setBookingId( booking.getBookingId() );
        if ( booking.getPaymentStatus() != null ) {
            bookingResponseDTO.setPaymentStatus( booking.getPaymentStatus().name() );
        }

        return bookingResponseDTO;
    }

    protected Event bookingRequestDTOToEvent(BookingRequestDTO bookingRequestDTO) {
        if ( bookingRequestDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setEventId( bookingRequestDTO.getEventId() );

        return event;
    }

    protected User bookingRequestDTOToUser(BookingRequestDTO bookingRequestDTO) {
        if ( bookingRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserId( bookingRequestDTO.getCustomerId() );

        return user;
    }

    private Integer bookingEventEventId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Event event = booking.getEvent();
        if ( event == null ) {
            return null;
        }
        Integer eventId = event.getEventId();
        if ( eventId == null ) {
            return null;
        }
        return eventId;
    }

    private String bookingEventTitle(Booking booking) {
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

    private BigDecimal bookingEventPrice(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Event event = booking.getEvent();
        if ( event == null ) {
            return null;
        }
        BigDecimal price = event.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
    }

    private Integer bookingCustomerUserId(Booking booking) {
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

    private String bookingCustomerFullName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        User customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String fullName = customer.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }
}
