package Model;

import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.MemberNotFoundException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a meeting Room that belongs to a Building.
 */
public class Room implements Serializable {
    private final InputValidator inputValidator;
    private String name;
    private Building building;
    private HashMap<String, ArrayList<BookingDateTime>> bookingSchedule;

    /**
     * This is the class constructor for creating a Room.
     *
     * @param name     is the name of the room.
     * @param building is the building details where it belongs.
     */
    public Room(String name, Building building) {
        this.name = name;
        this.building = building;
        this.bookingSchedule = new HashMap<>();
        this.inputValidator = new InputValidator();
    }

    /**
     * This method is to retrieve the name of the room.
     *
     * @return the name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * This method is to set the name of the room.
     *
     * @param name is the name of the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is to retrieve the building where room is.
     *
     * @return building details.
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * This method is to set the building where room is.
     *
     * @param building building details.
     */
    public void setBuilding(Building building) {
        this.building = building;
    }

    /**
     * This method is to retrieve all booking schedules for the room.
     *
     * @return all booking schedules for the room.
     */
    public HashMap<String, ArrayList<BookingDateTime>> getBookingSchedule() {
        return bookingSchedule;
    }

    /**
     * This method is to set all booking schedules for the room.
     *
     * @param bookingSchedule is the room booking schedule.
     */
    public void setBookingSchedule(HashMap<String, ArrayList<BookingDateTime>> bookingSchedule) {
        this.bookingSchedule = bookingSchedule;
    }

    /**
     * This method is to book a room and to keep record of booked datetime.
     * @param date is the date for which a booking has to be made.
     * @param startTime is the start time for the booking.
     * @param endTime is the end time for the booking.
     * @param email is the member's email id.
     * @throws MemberNotFoundException
     * @throws DateTimeInvalidFormat
     * @throws ParseException
     */
    public void addBookingSchedule(String date, String startTime, String endTime, String email) throws MemberNotFoundException, DateTimeInvalidFormat, ParseException {
        UniversityMember member = this.getBuilding().getUniversity().getMembers().get(email);
        if (member != null) {
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
            Date incomingStartDate = sf.parse(date + ";" + startTime);
            Date incomingEndDate = sf.parse(date + ";" + endTime);

            ArrayList<BookingDateTime> bookedTimeSlotsForDate = bookingSchedule.get(date);
            if (bookedTimeSlotsForDate != null) {
                for (BookingDateTime b : bookedTimeSlotsForDate) {
                    if (((incomingStartDate).after(b.getStartTime()) && (incomingEndDate).before(b.getEndTime())) ||
                            ((incomingStartDate).before(b.getStartTime()) && (incomingEndDate).after(b.getEndTime())) ||
                            ((incomingStartDate).after(b.getStartTime()) && (incomingStartDate).before(b.getEndTime())) ||
                            b.getStartTime().equals(incomingStartDate) ||
                            b.getEndTime().equals(incomingStartDate)) {
                        throw new DateTimeInvalidFormat("Check the schedule. Overlapping Time Slot");
                    }
                }
            }

            BookingDateTime newBooking = new BookingDateTime(date, incomingStartDate, incomingEndDate, member);
            ArrayList<BookingDateTime> existingBookingsForDate = bookingSchedule.get(date);
            if (existingBookingsForDate != null) {
                existingBookingsForDate.add(newBooking);
            } else {
                ArrayList<BookingDateTime> newArrList = new ArrayList<>();
                newArrList.add(newBooking);
                bookingSchedule.put(date, newArrList);
            }
        } else {
            throw new MemberNotFoundException("Member not registered with this email");
        }
    }

    /**
     * This method is to make a room booking.
     * @param email is the member's email id.
     * @param date is the booking date in the format <dd-mm-yyyy>.
     * @param startTime is the start of booking time.
     * @param endTime is the end of booking time.
     * @throws MemberNotFoundException
     * @throws DateTimeInvalidFormat
     * @throws ParseException
     */
    public void bookRoom(String email, String date, String startTime, String endTime) throws MemberNotFoundException, DateTimeInvalidFormat, ParseException {
        try {
            if (inputValidator.isValidDateTime(date, startTime, endTime)) {
                this.addBookingSchedule(date, startTime, endTime, email);
            } else {
                throw new DateTimeInvalidFormat("Date or Time have invalid formats");
            }
        } catch (MemberNotFoundException | DateTimeInvalidFormat e) {
            throw e;
        } catch (ParseException e) {
            throw new DateTimeInvalidFormat("Date or Time have invalid formats");
        }
    }

    /**
     * This method is to remove a booking made for a time period by a member.
     *
     * @param email     is the emailId of the member.
     * @param date      is the date string of format <dd-mm-yyyy>
     * @param startTime is the start time string of format <hh:mm>
     * @param endTime   is the end time string of format <hh:mm>
     */
    public void removeBookingInfo(String email, String date, String startTime, String endTime) throws MemberNotFoundException, ParseException {
        UniversityMember member = this.getBuilding().getUniversity().getMembers().get(email);
        if (member != null) {
            ArrayList<BookingDateTime> roomBookingsForDate = bookingSchedule.get(date.trim());
            for (BookingDateTime booking : roomBookingsForDate) {
                if (booking.getBookedBy().getEmailId().equals(email)) {
                    SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                    Date incomingStartDate = sf.parse(date + ";" + startTime);
                    Date incomingEndDate = sf.parse(date + ";" + endTime);
                    if (booking.getStartTime().equals(incomingStartDate) && booking.getEndTime().equals(incomingEndDate)) {
                        roomBookingsForDate.remove(booking);
                        break;
                    }
                }
            }
        } else {
            throw new MemberNotFoundException("Member not registered with this email");
        }
    }

    /**
     * This method is to remove bookings by email id.
     * @param emailId is the member's email id.
     * @throws MemberNotFoundException
     */
    public void removeAllBookingsByEmail(String emailId) throws MemberNotFoundException {
            String emailIdLowerCase = emailId.toLowerCase();
            UniversityMember member = this.getBuilding().getUniversity().getMembers().get(emailIdLowerCase);
            if (member != null) {
                for (ArrayList<BookingDateTime> b : this.getBookingSchedule().values()) {
                    for (int i = 0; i < b.size(); i++) {
                        if (b.get(i).getBookedBy().getEmailId().equals(emailIdLowerCase)) {
                            b.remove(i);
                        }
                    }
                }
            } else {
                throw new MemberNotFoundException("Member not registered with this email");
            }
    }

    /**
     * This method is to view bookings made by a member by email id.
     * @param emailId is the email id of the member.
     * @return all bookings made by a specific member.
     * @throws MemberNotFoundException
     */
    public List<BookingDateTime> listScheduleBookedByEmail(String emailId) throws MemberNotFoundException {
        List<BookingDateTime> bookedSchedules = new ArrayList<>();
        UniversityMember member = this.getBuilding().getUniversity().getMembers().get(emailId);
        if (member != null) {
            for (ArrayList<BookingDateTime> b : bookingSchedule.values()) {
                for (BookingDateTime bt : b) {
                    if (bt.getBookedBy().getEmailId().equals(emailId.toLowerCase())) {
                        bookedSchedules.add(bt);
                    }
                }
            }
            return bookedSchedules;
        } else {
            throw new MemberNotFoundException("Member not registered with this email");
        }
    }

    /**
     * This method is to fetch all formatted booked schedules of a room.
     * @return booked schedules of the room.
     */
    public List<String> getAllBookingSchedule() {
        List<String> bookedSchedules = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        for (ArrayList<BookingDateTime> b : bookingSchedule.values()) {
            for (BookingDateTime bt : b) {
                String strTime = dateFormat.format(bt.getStartTime());
                String endTime = dateFormat.format(bt.getEndTime());
                bookedSchedules.add(bt.getDate() + " | " + strTime + " | " + endTime + " - " + bt.getBookingStatus());
            }
        }
        return bookedSchedules;
    }
}
