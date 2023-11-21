package com.project.bullish.pedro.garcia.api.exception;

public class BullishNullCartException extends Exception {


    private final String interactionReason;
    private final String key;

    public BullishNullCartException(String message, String interactionReason, String key) {
        super(message);
        this.interactionReason = interactionReason;
        this.key = key;
    }

    public BullishNullCartException(String message) {
        this(message, null, null);
    }

    public String getInteractionReason() {
        return interactionReason;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return String.format("%s (%s, %s)", super.getMessage(), getInteractionReason(), getKey());
    }
}
