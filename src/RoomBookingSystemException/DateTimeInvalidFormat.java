package RoomBookingSystemException;

import java.io.Serializable;

public class DateTimeInvalidFormat extends Exception implements Serializable {
    public DateTimeInvalidFormat(String message) {
        super(message);
    }
}
