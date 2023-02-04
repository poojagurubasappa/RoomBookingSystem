package Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * This class is to validate inputs for Room Booking System.
 */
public class InputValidator implements Serializable {

    private String MIN_BOOKING_MIN = "5m";
    private int BOOKING_TIME_MODULUS = 5;

    /**
     * This method is to retrieve the valid time interval for booking a room.
     *
     * @return the valid time interval for booking a room.
     */
    public int getBOOKING_TIME_MODULUS() {
        return BOOKING_TIME_MODULUS;
    }

    /**
     * This method is to set the valid time interval for booking a room.
     *
     * @param BOOKING_TIME_MODULUS the valid time interval for booking a room.
     */
    public void setBOOKING_TIME_MODULUS(int BOOKING_TIME_MODULUS) {
        this.BOOKING_TIME_MODULUS = BOOKING_TIME_MODULUS;
    }

    /**
     * This method is to retrieve the allowed minimum booking time for a room.
     *
     * @return the allowed minimum booking time for a room.
     */
    public String getMIN_BOOKING_MIN() {
        return MIN_BOOKING_MIN;
    }

    /**
     * This method is to set the allowed minimum booking time for a room.
     *
     * @param MIN_BOOKING_MIN the allowed minimum booking time for a room.
     */
    public void setMIN_BOOKING_MIN(String MIN_BOOKING_MIN) {
        this.MIN_BOOKING_MIN = MIN_BOOKING_MIN;
    }

    /**
     * This method is to check if datetime string is not in the past and invalid format.
     * @param date is the input date dd-mm-yyyy.
     * @param startTime is the start of the time hh:mm.
     * @param endTime is the end of the time of format hh:mm
     * @return true or false
     */
    public boolean isValidDateTime(String date, String startTime, String endTime) {
        try {
            if (isValidDate(date) && isValidTime(startTime) && isValidTime(endTime)) {
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date incomingStartDateTime = sf.parse(date + ";" + startTime);
                Date incomingEndDateTime = sf.parse(date + ";" + endTime);
                Date currentDateTime = new Date();
                SimpleDateFormat currFormat = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date t = currFormat.parse(currFormat.format(currentDateTime));
                return !incomingStartDateTime.before(t) && !incomingEndDateTime.before(incomingStartDateTime);
            }
            return false;
        } catch (ParseException e) {
            return false;
        }

    }

    /**
     * This method is to check if the input date is in the right format and valid
     * @param date is the date string dd-mm-yyyy.
     * @param time is the time string hh:mm
     * @return true or false
     */
    public boolean isValidDateTime(String date, String time) {
        try {
            if (isValidDate(date) && isValidTime(time)) {
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date incomingDateTime = sf.parse(date + ";" + time);
                Date currentDateTime = new Date();
                SimpleDateFormat currFormat = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date t = currFormat.parse(currFormat.format(currentDateTime));
                return !incomingDateTime.before(t);
            }
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * This method is to check if the input date is in the expected format.
     * @param inputDate is the date string input
     * @return true or false
     */
    public boolean isValidDate(String inputDate) {
        Pattern validDatePattern = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
        if (validDatePattern.matcher(inputDate).matches()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            try {
                simpleDateFormat.parse(inputDate);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * This method is to check if the time is in expected format.
     * @param inputTime is the time string input
     * @return true or false
     */
    public boolean isValidTime(String inputTime) {
        try {
            String[] timeComponents = inputTime.split(":");
            if (timeComponents.length == 2) {
                int hour = Integer.parseInt(timeComponents[0]);
                int min = Integer.parseInt(timeComponents[1]);
                return (hour >= 0 && hour <= 24 && min >= 0 && min <= 60) || (min % this.getBOOKING_TIME_MODULUS() == 0);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method is to check if email is in the expected format.
     * @param email is the member's email
     * @return true or false
     */
    public boolean isValidEmailFormat(String email) {
        return Pattern.matches("^\\S+@\\S+\\.\\S+$", email);
    }

    /**
     * This method is to check if the string has no whitespaces or is not null.
     * @param input is the input string
     * @return true or false
     */
    public boolean isEmptyString(String input) {
        return input == null || input.trim().length() == 0;
    }
}
