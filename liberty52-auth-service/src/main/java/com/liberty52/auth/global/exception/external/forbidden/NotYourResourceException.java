package com.liberty52.auth.global.exception.external.forbidden;

import com.liberty52.common.exception.external.forbidden.ForbiddenException;

public class NotYourResourceException extends ForbiddenException {
    public NotYourResourceException(String resourceName, String id) {
        super(makeMessage(resourceName, id));
    }

    private static String makeMessage(String resourceName, String id) {
        return String.format("The %s is not for the user. The user.id was %s", resourceName, id);
    }
}
