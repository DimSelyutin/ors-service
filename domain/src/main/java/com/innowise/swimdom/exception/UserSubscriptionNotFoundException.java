<<<<<<< Updated upstream:domain/src/main/java/com/innowise/swimdom/exception/UserSubscriptionNotFoundException.java
package com.innowise.swimdom.exception;
=======
package com.innowise.swimdom.exceptions;
>>>>>>> Stashed changes:domain/src/main/java/com/innowise/swimdom/exceptions/UserSubscriptionNotFoundException.java

/**
 * An exception indicating that the user sent wrong request.
 * Error code: 400.
 *
 */
public class UserSubscriptionNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public UserSubscriptionNotFoundException(String message) {
        super(message);
    }
}
