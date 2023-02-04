package Model;

import RoomBookingSystemException.MemberNotFoundException;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * This class represents a member of the university.
 */
public class UniversityMember implements Serializable {
    private final University university;
    private String name;
    private String emailId;

    /**
     * This is the class constructor for the member of University.
     *
     * @param name    is the name of the member.
     * @param emailId is a unique email of the member.
     */
    public UniversityMember(String name, String emailId, University university) {
        this.name = name;
        this.emailId = emailId;
        this.university = university;
    }

    /**
     * This method is to retrieve the name of the University member.
     *
     * @return the name of the University member.
     */
    public String getName() {
        return name;
    }

    /**
     * This method assigns the name to the University member.
     *
     * @param name is the name of the University member.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is to retrieve the emailId of the University member.
     *
     * @return the emailId of the University member.
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * This method is to assign the emailId to the University member.
     *
     * @param emailId is the emailId of the University member.
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * This method is to retrieve the list of rooms booked by the University member.
     *
     * @return the list of rooms booked by the University member.
     */
    public ArrayList<Room> getBookedRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        for (Building building : this.university.getBuildings().values()) {
            for (Room room : building.getRooms().values()) {
                for (ArrayList<BookingDateTime> b : room.getBookingSchedule().values()) {
                    for (BookingDateTime b1 : b) {
                        if (b1.getBookedBy().getEmailId().equals(this.getEmailId())) {
                            rooms.add(room);
                        }
                    }
                }
            }
        }
        return rooms;
    }

    /**
     * This method is to remove all booked rooms by email.
     * @throws MemberNotFoundException
     */
    public void removeAllBookedRooms() throws MemberNotFoundException {
        for (Building building : this.university.getBuildings().values()) {
            for (Room room : building.getRooms().values()) {
                room.removeAllBookingsByEmail(emailId);
            }
        }
    }

    /**
     * This method is to cancel the booking of a room.
     * @param roomName is the name of the room
     * @param buildingName is the name of the building
     * @param date is the date of booking
     * @param start is the start time of the booking
     * @param end is the end time of the booking
     * @throws MemberNotFoundException
     * @throws ParseException
     */
    public void cancelBooking(String roomName, String buildingName, String date, String start, String end) throws MemberNotFoundException, ParseException {
        Room existingRoom = this.university.getBuildings().get(buildingName).getRooms().get(roomName);
        if (existingRoom != null) {
            existingRoom.removeBookingInfo(emailId, date, start, end);
        }
    }
}
