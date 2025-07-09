package com.booking.application.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.booking.application.dto.BookingRequestDTO;
import com.booking.application.dto.BookingResponseDTO;
import com.booking.application.entites.BookingEntity;
import com.booking.application.entites.BookingStatus;
import com.booking.application.entites.GuestEntity;
import com.booking.application.entites.PropertyEntity;
import com.booking.application.entites.UserEntity;
import com.booking.application.repositories.BookingRepository;
import com.booking.application.repositories.PropertyRepository;
import com.booking.application.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {
    private final UserRepository user_repo;
    private final PropertyRepository property_repo;
    private final BookingRepository booking_repo;

    @Autowired
    public BookingService(UserRepository user_repo,
            PropertyRepository property_repo,
            BookingRepository booking_repo) {
        this.user_repo = user_repo;
        this.property_repo = property_repo;
        this.booking_repo = booking_repo;
    }

    // implement exception handling and implement AOP concepts as well!!
    @RedisLock(ttl = 6000)
    @Transactional
    public BookingEntity createBooking(BookingRequestDTO bookingRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = user_repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found\n"));
        System.out.println("go the user details ..\n");
        PropertyEntity property = property_repo.findById(bookingRequest.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found \n"));
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        booking.setProperty(property);
        booking.setCheakIn(bookingRequest.getCheakIn());
        booking.setCheakOut(bookingRequest.getCheakOut());
        booking.setStatus(BookingStatus.INITIATED);
        long days = getDaysBetween(bookingRequest.getCheakIn(), bookingRequest.getCheakOut());
        double pricePayable = days * property.price_per_night;
        booking.setTotalprice(pricePayable);

        List<GuestEntity> guestList = bookingRequest.getGuestList().stream()
                .map(guestDTO -> {
                    GuestEntity guest = new GuestEntity();
                    guest.setName(guestDTO.getName());
                    guest.setAge(guestDTO.getAge());
                    guest.setBooking(booking);
                    return guest;
                }).collect(Collectors.toList());
        booking.setGuests(guestList);
        // cheak the db for overlapping booking to. and set the status to FAIL if not
        // available
        if (booking_repo.existsOverlappingBookings(property.getPropertyId(),
                bookingRequest.getCheakIn(),
                bookingRequest.getCheakOut()))
            throw new IllegalStateException("Overlapping Booking\n");
        return booking_repo.save(booking);

    }

    public static long getDaysBetween(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ChronoUnit.DAYS.between(localDate1, localDate2);
    }
    public List<BookingResponseDTO> getUserBookings(){
         String username=SecurityContextHolder.getContext().getAuthentication().getName();
         return booking_repo.getUserBookings(username);
    }
}
