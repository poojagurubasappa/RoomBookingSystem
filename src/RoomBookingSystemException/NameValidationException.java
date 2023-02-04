package RoomBookingSystemException;

import java.io.Serializable;

public class NameValidationException extends Exception implements Serializable {
    public NameValidationException(String str) {
        super(str);
    }
}
