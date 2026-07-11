package np.edu.nast.ebs.mapper;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.PaymentRequestDTO;
import np.edu.nast.ebs.dto.response.PaymentResponseDTO;
import np.edu.nast.ebs.model.Booking;
import np.edu.nast.ebs.model.Payment;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T09:41:02+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Payment payment = new Payment();

        if ( dto.getAmount() != null ) {
            payment.setAmount( BigDecimal.valueOf( dto.getAmount() ) );
        }

        return payment;
    }

    @Override
    public PaymentResponseDTO toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();

        paymentResponseDTO.setBookingId( paymentBookingBookingId( payment ) );
        paymentResponseDTO.setAmount( payment.getAmount() );
        paymentResponseDTO.setPaymentDate( payment.getPaymentDate() );
        paymentResponseDTO.setPaymentId( payment.getPaymentId() );
        if ( payment.getStatus() != null ) {
            paymentResponseDTO.setStatus( payment.getStatus().name() );
        }

        return paymentResponseDTO;
    }

    private Integer paymentBookingBookingId(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        Booking booking = payment.getBooking();
        if ( booking == null ) {
            return null;
        }
        Integer bookingId = booking.getBookingId();
        if ( bookingId == null ) {
            return null;
        }
        return bookingId;
    }
}
