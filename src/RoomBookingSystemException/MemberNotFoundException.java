package RoomBookingSystemException;

import java.io.Serializable;

public class MemberNotFoundException extends Exception implements Serializable {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
