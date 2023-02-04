package Model;

import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents University Building.
 */
public class Building implements Serializable {
    private final University university;
    private String name;
    private String address;
    private HashMap<String, Room> rooms;

    private InputValidator inputValidator;

    /**
     * This is the class constructor for Building that takes in its name, address and rooms.
     *
     * @param name    is the name of the Building
     * @param address is the address of the Building.
     */
    public Building(String name, String address, University university) {
        this.name = name;
        this.address = address;
        this.rooms = new HashMap<>();
        this.university = university;
        this.inputValidator = new InputValidator();
    }

    /**
     * This method is to return the university object.
     *
     * @return University object.
     */
    public University getUniversity() {
        return university;
    }

    /**
     * This method is to retrieve the name of the building.
     *
     * @return name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * This method is to set the name of the building.
     *
     * @param name is name of the building.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is to retrieve the address of the building.
     *
     * @return address of the building.
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method is to set the address of the building.
     *
     * @param address is address of the building.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method is to retrieve all the rooms in the building.
     *
     * @return all rooms in the building.
     */
    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    /**
     * This method is to set all the rooms in the building.
     *
     * @param rooms hashmap of all rooms in the building.
     */
    public void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }

    public List<String> getRoomNames() {
        List<String> roomNames = new ArrayList<>();
        for (Room room : this.rooms.values()) {
            roomNames.add(room.getName());
        }
        return roomNames;
    }

    /**
     * This method returns all the names of the rooms booked by a member.
     *
     * @param email is the emailId of the member.
     * @return a list of room names where there are bookings.
     * @throws MemberNotFoundException when the member is not registered with university.
     */
    public List<String> getBookedRoomsByEmail(String email) throws MemberNotFoundException {
        List<String> roomNames = new ArrayList<>();
        for (Room room : this.rooms.values()) {
            if (room.listScheduleBookedByEmail(email).size() != 0) {
                roomNames.add(room.getName());
            }
        }
        return roomNames;
    }

    /**
     * This method returns a list of a room's schedules booked by a member.
     *
     * @param roomName is the room for which the schedules are returned.
     * @param email    is the emailId of the member.
     * @return a list of schedules for a room.
     * @throws MemberNotFoundException when the email is not valid.
     */
    public List<String> getRoomBookingSchedulesByEmail(String roomName, String email) throws MemberNotFoundException {
        List<String> schedules = new ArrayList<>();
        Room room = this.rooms.get(roomName);
        for (BookingDateTime b : room.listScheduleBookedByEmail(email)) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String strTime = dateFormat.format(b.getStartTime());
            String endTime = dateFormat.format(b.getEndTime());
            schedules.add(b.getDate() + " | " + strTime + " | " + endTime + " | " + room.getBuilding().getName() + " | " + room.getName());
        }
        return schedules;
    }

    /**
     * This method is to add a room to the building.
     *
     * @param roomName is the name of the room.
     */
    public void addRoom(String roomName) throws NameValidationException {
        String roomNameLowerCase = roomName != null ? roomName.toLowerCase() : "";
        if (roomNameLowerCase.trim().length() >= 1 && this.rooms.get(roomNameLowerCase) == null) {
            Room room = new Room(roomName, this);
            this.rooms.put(roomNameLowerCase, room);
        } else {
            throw new NameValidationException("This room name is taken or empty for this building. Please choose a new name");
        }
    }

    /**
     * This method is to remove a room from the building.
     *
     * @param roomName is the name of the room.
     */
    public void removeRoom(String roomName) {
        String roomNameLowerCase = roomName.toLowerCase();
        Room room = this.rooms.get(roomNameLowerCase);
        if (room != null) {
            this.rooms.remove(roomNameLowerCase);
        }
    }

    private boolean isValidDateTime(Date incomingStart, Date start, Date end) {
        if ((incomingStart.after(start) && incomingStart.before(end)) || (incomingStart.equals(start) || incomingStart.equals(end))) {
            return true;
        }
        return false;
    }

    /**
     * This method returns all available rooms for a given date and time.
     *
     * @param date is the date string of format <dd-mm-yyyy>
     * @param time is the time string of format <hh:mm>
     * @return list of available rooms.
     * @throws DateTimeInvalidFormat when the date and time are invalid.
     */
    public ArrayList<Room> getAvailableRoomsByStartTime(String date, String time) throws DateTimeInvalidFormat {
        try {
            if (!inputValidator.isValidDateTime(date, time)) {
                throw new DateTimeInvalidFormat("Invalid date or time format");
            }
            ArrayList<Room> availableRooms = new ArrayList<>();
            for (Room r : this.rooms.values()) {
                boolean isAvailableRoom = true;
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date incomingStartDateTime = sf.parse(date + ";" + time);
                if (r.getBookingSchedule().get(date) != null) {
                    for (BookingDateTime bt : r.getBookingSchedule().get(date)) {
                        Date startTime = bt.getStartTime();
                        Date endTime = bt.getEndTime();
                        if(isValidDateTime(incomingStartDateTime, startTime, endTime)) {
                            isAvailableRoom = false;
                            break;
                        }
                    }
                }
                if (isAvailableRoom) {
                    availableRooms.add(r);
                }
            }
            return availableRooms;
        } catch (ParseException e) {
            throw new DateTimeInvalidFormat("Invalid date or time format.");
        }
    }

    /**
     * This method returns all available rooms for a given date and time range.
     *
     * @param date  is the date string of format <dd-mm-yyyy>
     * @param start is the start time string of format <hh:mm>
     * @param end   is the end time string of format <hh:mm>
     * @return list of available rooms.
     * @throws DateTimeInvalidFormat when the date or time are of invalid format.
     */
    public ArrayList<Room> getAvailableRoomsByTimeRange(String date, String start, String end) throws DateTimeInvalidFormat {
        try {
            if (!inputValidator.isValidDateTime(date, start, end)) {
                throw new DateTimeInvalidFormat("Invalid date or time format");
            }
            ArrayList<Room> availableRooms = new ArrayList<>();
            for (Room r : this.rooms.values()) {
                boolean isAvailableRoom = true;
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm");
                Date incomingStartDate = sf.parse(date + ";" + start);
                Date incomingEndDate = sf.parse(date + ";" + end);
                if (r.getBookingSchedule().get(date) != null) {
                    for (BookingDateTime bt : r.getBookingSchedule().get(date)) {
                        Date startTime = bt.getStartTime();
                        Date endTime = bt.getEndTime();
                        if(isValidDateTime(incomingStartDate, startTime, endTime) || (incomingEndDate.after(startTime) && incomingEndDate.before(endTime))) {
                            isAvailableRoom = false;
                            break;
                        }
                    }
                }
                if (isAvailableRoom) {
                    availableRooms.add(r);
                }
            }
            return availableRooms;
        } catch (ParseException e) {
            throw new DateTimeInvalidFormat("Invalid date or time format.");
        }
    }
}
