package RoomBookingSystemException;

import java.io.Serializable;

public class EmailValidationException extends Exception implements Serializable {
    public EmailValidationException(String str) {
        super(str);
    }
}
