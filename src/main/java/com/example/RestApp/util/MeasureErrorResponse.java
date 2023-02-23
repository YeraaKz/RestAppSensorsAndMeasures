package com.example.RestApp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class MeasureErrorResponse{
    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private long timestamp;
}
