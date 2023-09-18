package com.youtube.agent.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserPartialUpdate(
        @NotEmpty(message = "Country code is empty")
        String country,
        @NotNull(message = "Job time is empty")
        Integer jobTimeInMinutes) {
}
