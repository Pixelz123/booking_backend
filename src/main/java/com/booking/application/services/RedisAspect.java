package com.booking.application.services;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.booking.application.dto.BookingRequestDTO;
import com.booking.application.entites.BookingEntity;
import com.booking.application.entites.BookingStatus;

@Aspect
@Component
public class RedisAspect {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        Object[] args = joinPoint.getArgs();
        BookingRequestDTO booking = null;
        for (Object arg : args) {
            if (arg instanceof BookingRequestDTO bookingRequestDTO) {
                booking = bookingRequestDTO;
                break;
            }
        }
        if (booking == null)
            throw new IllegalArgumentException("Invalid Booking request\n");

        String propertyId = booking.getPropertyId();
        Date cheakIn = booking.getCheakIn();
        Date cheakOut = booking.getCheakOut();

        List<String> lockKeys = generateKeys(propertyId, cheakIn, cheakOut);
        String lockId = UUID.randomUUID().toString();
        long timeout = redisLock.ttl();

        boolean lockedAll = lockAll(lockKeys, lockId, timeout);
        if (!lockedAll) {
            throw new IllegalArgumentException("Booking Conflict due to concurrent booking ..\n");
        }
        try {
           BookingEntity bookingIssue = (BookingEntity) joinPoint.proceed();
           bookingIssue.setStatus(BookingStatus.SUCCESS);
           return bookingIssue;
        } catch (Exception e) {
             throw e;
        } finally {
            unlockAll(lockKeys, lockId);
        }
    }

    private List<String> generateKeys(String propertyId, Date checkIn, Date checkOut) {
        List<String> keys = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar start = Calendar.getInstance();
        start.setTime(checkIn);

        Calendar end = Calendar.getInstance();
        end.setTime(checkOut);

        while (!start.after(end)) {
            String dateStr = sdf.format(start.getTime());
            String redisKey = "booking-lock:" + propertyId + ":" + dateStr;
            keys.add(redisKey);
            start.add(Calendar.DATE, 1);
        }

        return keys;
    }

    private boolean lockAll(List<String> keys, String lockId, long timeoutMs) {
        List<String> acquired = new ArrayList<>();
        try {
            for (String key : keys) {
                Boolean success = redisTemplate.opsForValue()
                        .setIfAbsent(key, lockId, Duration.ofMillis(timeoutMs));
                if (Boolean.TRUE.equals(success)) {
                    acquired.add(key);
                } else {
                    unlockAll(acquired, lockId);
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            unlockAll(acquired, lockId);
            return false;
        }
    }

    private void unlockAll(List<String> keys, String lockId) {
        for (String key : keys) {
            Object val = redisTemplate.opsForValue().get(key);
            if (lockId.equals(val)) {
                redisTemplate.delete(key);
            }
        }
    }

}
