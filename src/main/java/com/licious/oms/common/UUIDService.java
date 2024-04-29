package com.licious.oms.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UUIDService {
    /**
     * Generates a random UUID (Universally Unique Identifier).
     *
     * @return A randomly generated UUID.
     */
    public UUID generateUUID() {
        synchronized (this) {
            return UUID.randomUUID();
        }
    }
}
