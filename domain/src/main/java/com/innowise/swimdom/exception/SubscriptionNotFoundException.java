<<<<<<< Updated upstream:domain/src/main/java/com/innowise/swimdom/exception/SubscriptionNotFoundException.java
package com.innowise.swimdom.exception;
=======
package com.innowise.swimdom.exceptions;
>>>>>>> Stashed changes:domain/src/main/java/com/innowise/swimdom/exceptions/SubscriptionNotFoundException.java

/**
 * An exception indicating that the user sent wrong request.
 * Error code: 400.
 *
 */
public class SubscriptionNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
