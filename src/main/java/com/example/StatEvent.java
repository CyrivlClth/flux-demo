package com.example;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class StatEvent {
    private final String queryId;
    private final LocalDateTime timestamp;
}
