package com.rocketseat.planner.ExceptionHandling;

import com.rocketseat.planner.activity.ActivityResponse;

public class ErrorActivityResponse {
    private final ActivityResponse activityResponse; // Composição da ActivityResponse
    private final String errorMessage;

    // Constructor
    public ErrorActivityResponse(ActivityResponse activityResponse, String errorMessage) {
        this.activityResponse = activityResponse;
        this.errorMessage = errorMessage;
    }
}
