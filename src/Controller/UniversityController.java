package Controller;

import Model.Building;
import Model.Room;
import Model.University;
import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;
import View.CliView;
import View.GuiView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the controller intefacing the data from model to view.
 */
public class UniversityController {
    private final GuiView gui;
    private final CliView cli;
    private University universityModel;

    /**
     * This is the class constructor.
     * @param university is the model object.
     * @param gui is for the Graphical User Interface, one of the views.
     * @param cli is for the Graphical User Interface, one of the views.
     */
    public UniversityController(University university, GuiView gui, CliView cli) {
        this.universityModel = university;
        this.gui = gui;
        this.cli = cli;
        this.gui.setController(this);
        this.cli.setController(this);
    }

    /**
     * This method is to add a member to university.
     * @param name is the name of the member.
     * @param emailId is the email id of the member.
     * @throws EmailValidationException
     * @throws NameValidationException
     */
    public void insertPerson(String name, String emailId) throws EmailValidationException, NameValidationException {
        universityModel.addMember(name, emailId);
    }

    /**
     * This method is to add a building to the university.
     * @param name is the name of the building
     * @param address is the address of the building
     * @throws NameValidationException
     */
    public void insertBuilding(String name, String address) throws NameValidationException {
        universityModel.addBuilding(name, address);
    }

    /**
     * This method is to list all building names of the university.
     * @return list of building names.
     */
    public List<String> listBuildings() {
        return universityModel.listBuildingNames();
    }

    /**
     * This method is to add room in a specific building of the university.
     * @param buildingName is the name of the building
     * @param roomName is the name of the room
     * @throws NameValidationException
     */
    public void insertRoom(String buildingName, String roomName) throws NameValidationException {
        universityModel.getBuildings().get(buildingName.toLowerCase()).addRoom(roomName);
    }

    /**
     * This method is to list all rooms in a specific building of the university.
     * @param buildingName is the name of the building
     * @return
     */
    public List<String> listRooms(String buildingName) {
        return universityModel.getBuildings().get(buildingName.toLowerCase()).getRoomNames();
    }

    /**
     * This method is to book a room.
     * @param buildingName is the name of the building
     * @param roomName is the name of the room
     * @param email is the email of the member booking
     * @param date is the booking date
     * @param start is the start time of booking of format hh:mm
     * @param end is the end time of booking of format hh:mm
     * @throws Exception
     */
    public void bookARoom(String buildingName, String roomName, String email, String date, String start, String end) throws Exception {
        try {
            universityModel.getBuildings().get(buildingName.toLowerCase()).getRooms().get(roomName.toLowerCase()).bookRoom(email, date, start, end);
        } catch (MemberNotFoundException | DateTimeInvalidFormat e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to list all the buildings where reservations are made by a specific member by email.
     * @param email is the email id of a member.
     * @return list of buildings where a member has bookings.
     * @throws MemberNotFoundException
     */
    public List<String> listBuildingsBookedByEmail(String email) throws MemberNotFoundException {
        return universityModel.listBuildingsBookedByEmail(email);
    }

    /**
     * This method is to list all the rooms in a specific building
     * where reservations are made by a specific member by email.
     * @param buildingName is the name of the building
     * @param email is the email id of a member.
     * @return list of rooms in a building where a member has bookings.
     * @throws Exception
     */
    public List<String> listRoomsBookedByEmail(String buildingName, String email) throws Exception {
        try {
            return universityModel.getBuildings().get(buildingName.toLowerCase()).getBookedRoomsByEmail(email);
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to list all the room bookings a member holds by email.
     * @param buildingName is the name of the building
     * @param roomName is the name of the room
     * @param email is the email id of the member
     * @return a list of scheduled bookings a member has made in a room in a building.
     * @throws Exception
     */
    public List<String> listRoomSchedulesByEmail(String buildingName, String roomName, String email) throws Exception {
        try {
            return universityModel.getBuildings().get(buildingName.toLowerCase()).getRoomBookingSchedulesByEmail(roomName, email);
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to cancel a booking made by a member by email.
     * @param buildingName is the name of the building.
     * @param roomName is the name of the room.
     * @param email is the email id of a member.
     * @param date is the date of booking.
     * @param start is the start of booking time.
     * @param end is the end of booking time.
     * @throws Exception
     */
    public void removeBooking(String buildingName, String roomName, String email, String date, String start, String end) throws Exception {
        try {
            universityModel.getBuildings().get(buildingName.toLowerCase()).getRooms().get(roomName.toLowerCase()).removeBookingInfo(email, date, start, end);
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to remove a member by emailId from university.
     * @param email is the email id of the member.
     * @throws MemberNotFoundException
     */
    public void removeMember(String email) throws MemberNotFoundException {
        universityModel.removeMember(email);
    }

    /**
     * This method is to remove a building from university.
     * @param buildingName is the name of the building
     */
    public void removeBuilding(String buildingName) {
        universityModel.removeBuilding(buildingName);
    }

    /**
     * This method is to remove a room from the university.
     * @param buildingName is the name of the building.
     * @param roomName is the name of the room
     * @throws Exception
     */
    public void removeRoom(String buildingName, String roomName) throws Exception {
        try {
            universityModel.getBuildings().get(buildingName.toLowerCase()).removeRoom(roomName);
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to list the available rooms for a given start time.
     * @param date is the date to see availability.
     * @param time is the start time.
     * @return a list of available rooms.
     * @throws DateTimeInvalidFormat
     */
    public HashMap<String, ArrayList<String>> getAvailableRoomsForStartTime(String date, String time) throws DateTimeInvalidFormat {
        HashMap<String, ArrayList<String>> availableRoomsByBuilding = new HashMap<>();
        for (Building b : universityModel.getBuildings().values()) {
            ArrayList<String> roomNames = new ArrayList<>();
            for (Room r : b.getAvailableRoomsByStartTime(date, time)) {
                roomNames.add(r.getName());
            }
            availableRoomsByBuilding.put(b.getName(), roomNames);
        }
        return availableRoomsByBuilding;
    }

    /**
     * This method is to list the available rooms for a given time range.
     * @param date is the date to see availability.
     * @param start is the start time.
     * @param end is the end time.
     * @return a list of available rooms.
     * @throws DateTimeInvalidFormat
     */
    public HashMap<String, ArrayList<String>> getAvailableRoomsForTimeRange(String date, String start, String end) throws DateTimeInvalidFormat {
        HashMap<String, ArrayList<String>> availableRoomsByBuilding = new HashMap<>();
        System.out.println("here!!");
        for (Building b : universityModel.getBuildings().values()) {
            ArrayList<String> roomNames = new ArrayList<>();
            for (Room r : b.getAvailableRoomsByTimeRange(date, start, end)) {
                roomNames.add(r.getName());
            }
            availableRoomsByBuilding.put(b.getName(), roomNames);
        }
        return availableRoomsByBuilding;
    }

    /**
     * This method is to list the booking schedule of a given room.
     * @param buildingName is the name of the building
     * @param roomName is the name of the room
     * @return a list of booked schedules for a room
     * @throws Exception
     */
    public List<String> getRoomBookingSchedule(String buildingName, String roomName) throws Exception {
        try {
            return universityModel.getBuildings().get(buildingName.toLowerCase()).getRooms().get(roomName.toLowerCase()).getAllBookingSchedule();
        } catch (Exception e) {
            throw new Exception("Sorry something went wrong. There are probably no members, buildings or rooms registered with the university");
        }
    }

    /**
     * This method is to view all rooms booked by a member by email.
     * @param email is the email id of the member
     * @return a list of rooms booked by email.
     * @throws Exception
     */
    public List<String> listAllRoomsBookedByEmail(String email) throws Exception {
        List<String> s = new ArrayList<>();
        for (String building : this.listBuildingsBookedByEmail(email)) {
            for (String room : this.listRoomsBookedByEmail(building, email)) {
                s.addAll(this.listRoomSchedulesByEmail(building, room, email));
            }
        }
        return s;
    }

    /**
     * This method is to save all the data in a file.
     */
    public void saveAllData() {
        try {
            University university = this.universityModel;
            FileOutputStream buildingsStr =
                    new FileOutputStream("uni.ser");
            ObjectOutputStream buildingsOOS = new ObjectOutputStream(buildingsStr);

            buildingsOOS.writeObject(university);
            buildingsOOS.close();
            buildingsStr.close();

        } catch (IOException e) {

        }
    }

    /**
     * This method is to load all data from a file.
     */
    @SuppressWarnings("unchecked")
    public void loadAllData() {
        try {

            FileInputStream buildingsFIS = new FileInputStream("uni.ser");
            ObjectInputStream buildingsOIS = new ObjectInputStream(buildingsFIS);

            this.universityModel = (University) buildingsOIS.readObject();

            buildingsOIS.close();
            buildingsFIS.close();

        } catch (Exception e) {

        }
    }
}
