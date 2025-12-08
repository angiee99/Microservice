package ang.mois.pc.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception when the entity cannot be deleted as some other entity references it in the FK.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class FKConflictException extends RuntimeException {
    public FKConflictException(String message) {
        super(message);
    }
}
