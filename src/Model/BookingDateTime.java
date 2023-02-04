package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class BookingDateTime implements Serializable {
    private String date;
    private Date startTime;
    private Date endTime;

    private final ArrayList<String> bookingRange;
    private String bookingStatus;
    private UniversityMember bookedBy;

    public BookingDateTime(String date, Date startTime, Date endTime, UniversityMember bookedBy) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.bookingStatus = "BOOKED";
        this.bookingRange = this.computeBookingRange();
    }

    private ArrayList<String> computeBookingRange() {
        ArrayList<String> bookingTimeRange = new ArrayList<>();

        return bookingTimeRange;
    }

    /**
     * This method is to retrieve the booking date.
     *
     * @return the booking date.
     */
    public String getDate() {
        return date;
    }

    /**
     * This method is to set the booking date.
     *
     * @param date the booking date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method is to retrieve the booking start time.
     *
     * @return the booking start time.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method is to set the booking start time.
     *
     * @param startTime the booking start time.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method is to retrieve the booking end time.
     *
     * @return the booking end time.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method is to set the booking end time.
     *
     * @param endTime the booking end time.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method is to retrieve the details of who booked the room.
     *
     * @return the details of who booked the room.
     */
    public UniversityMember getBookedBy() {
        return bookedBy;
    }

    /**
     * This method is to set the details of who booked the room.
     *
     * @param bookedBy the details of who booked the room.
     */
    public void setBookedBy(UniversityMember bookedBy) {
        this.bookedBy = bookedBy;
    }

    /**
     * This method is to retrieve the status of the room.
     *
     * @return Available or Booked.
     */
    public String getBookingStatus() {
        return bookingStatus != null ? bookingStatus : "AVAILABLE";
    }

    /**
     * This method is to set the status of the room to "BOOKED".
     */
    public void setBookingStatus() {
        this.bookingStatus = "BOOKED";
    }
}
