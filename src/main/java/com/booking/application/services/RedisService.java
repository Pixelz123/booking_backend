package com.booking.application.services;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class RedisService {
    private final String REDIS_SERVER_IP = "127.0.0.1";
    private final int REDIS_SERVER_PORT = 5761;
    private final Jedis jedis = new Jedis(REDIS_SERVER_IP, REDIS_SERVER_PORT);
}
