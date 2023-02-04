package Model;

import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.NameValidationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BuildingTest {
    University uni = new University("University of St Andrews", "St Andrews");
    Building building = new Building("Balfour", "DRA", uni);

    @Test
    public void getUniversity() {
        assertEquals("University of St Andrews", building.getUniversity().getName());
    }

    @Test
    public void getName() {
        assertEquals("Balfour", building.getName());
    }

    @Test
    public void setName() {
        building.setName("Sellars");
        assertEquals("Sellars", building.getName());
    }

    @Test
    public void getAddress() {
        assertEquals(building.getAddress(), "DRA");
    }

    @Test
    public void setAddress() {
        building.setAddress("Market Street");
        assertEquals("Market Street", building.getAddress());
    }

    @Test
    public void getRooms() throws NameValidationException {
        building.addRoom("0102");
        building.addRoom("0103");

        assertEquals(2, building.getRooms().size());
        assertTrue(building.getRooms().containsKey("0102"));
        assertTrue(building.getRooms().containsKey("0103"));

        building.getRooms().clear();
    }

    @Test
    public void setRooms() {
        HashMap<String, Room> rooms = new HashMap<>();
        Room room1 = new Room("0102", building);
        Room room2 = new Room("0103", building);
        rooms.put("0102", room1);
        rooms.put("0103", room2);

        building.setRooms(rooms);
        assertEquals(rooms.size(), building.getRooms().size());

        building.getRooms().clear();
    }

    @Test
    public void getRoomNames() throws NameValidationException {
        building.addRoom("0102");
        building.addRoom("0103");

        assertTrue(building.getRooms().containsKey("0102"));
        assertTrue(building.getRooms().containsKey("0103"));

        building.getRooms().clear();
    }

    @Test
    public void getBookedRoomsByEmail() throws Exception {
        uni.addMember("testUser", "test@test.com");
        building.addRoom("0102");
        building.addRoom("0103");
        building.addRoom("0104");
        building.getRooms().get("0102").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");
        building.getRooms().get("0103").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");

        assertTrue(building.getBookedRoomsByEmail("test@test.com").contains("0102"));
        assertTrue(building.getBookedRoomsByEmail("test@test.com").contains("0103"));
        assertFalse(building.getBookedRoomsByEmail("test@test.com").contains("0104"));

        building.getRooms().clear();
    }

    @Test
    public void getRoomBookingSchedulesByEmail() throws Exception {
        uni.addMember("testUser", "test@test.com");
        building.addRoom("0102");
        building.addRoom("0103");
        building.addRoom("0104");
        building.getRooms().get("0102").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");
        building.getRooms().get("0103").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");

        assertTrue(building.getRoomBookingSchedulesByEmail("0102","test@test.com").contains("12-12-2022 | 08:10 | 08:20 | Balfour | 0102"));
        assertTrue(building.getRoomBookingSchedulesByEmail("0103","test@test.com").contains("12-12-2022 | 08:10 | 08:20 | Balfour | 0103"));
        assertFalse(building.getRoomBookingSchedulesByEmail("0104","test@test.com").contains("12-12-2022 | 08:10 | 08:20 | Balfour | 0104"));

        building.getRooms().clear();
    }

    @Test
    public void addRoom() throws NameValidationException {
        assertEquals(0, building.getRooms().size());
        building.addRoom("0101");
        assertEquals(1, building.getRooms().size());
        String roomName = "SubWay";
        building.addRoom("SubWay");
        assertTrue(building.getRooms().containsKey(roomName.toLowerCase()));

        building.getRooms().clear();
    }

    @Test(expected = NameValidationException.class)
    public void addRoom_nameCannotBeNull() throws NameValidationException {
        building.addRoom(null);
    }

    @Test(expected = NameValidationException.class)
    public void addRoom_nameCannotBeEmpty() throws NameValidationException {
        building.addRoom(" ");
    }

    @Test(expected = NameValidationException.class)
    public void addRoom_nameCannotBeDuplicateForABuilding() throws NameValidationException {
        building.addRoom("room");
        building.addRoom("room");

        building.getRooms().clear();
    }

    @Test
    public void removeRoom() throws NameValidationException {
        building.addRoom("0101");
        assertEquals(1, building.getRooms().size());
        building.removeRoom("0101");
        assertEquals(0, building.getRooms().size());
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void getAvailableRoomsByStartTime_dateCannotBeInvalid() throws DateTimeInvalidFormat {
        ArrayList<Room> rooms = building.getAvailableRoomsByStartTime("12-12-1111", "08:15");
    }

    @Test
    public void getAvailableRoomsByStartTime() throws Exception {
        uni.addMember("testUser", "test@test.com");
        building.addRoom("0102");
        building.addRoom("0103");
        building.addRoom("0104");
        building.getRooms().get("0102").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");
        building.getRooms().get("0103").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");

        ArrayList<Room> rooms = building.getAvailableRoomsByStartTime("12-12-2022", "08:15");

        boolean isInvalidResult = false;
        for(Room r: rooms) {
            String name = r.getName();
            if(!name.equals("0104")) {
                isInvalidResult = true;
                break;
            }
        }
        assertFalse(isInvalidResult);

        building.getRooms().clear();
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void getAvailableRoomsByTimeRange_dateCannotBeInvalid() throws DateTimeInvalidFormat {
        ArrayList<Room> rooms = building.getAvailableRoomsByTimeRange("12-12-1111", "08:15", "08:45");
    }

    @Test
    public void getAvailableRoomsByTimeRange() throws Exception {
        uni.addMember("testUser", "test@test.com");
        building.addRoom("0102");
        building.addRoom("0103");
        building.addRoom("0104");
        building.getRooms().get("0102").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");
        building.getRooms().get("0103").bookRoom("test@test.com", "12-12-2022", "08:10", "08:20");

        ArrayList<Room> rooms = building.getAvailableRoomsByTimeRange("12-12-2022", "08:15", "09:00");

        boolean isInvalidResult = false;
        for(Room r: rooms) {
            String name = r.getName();
            if(!name.equals("0104")) {
                isInvalidResult = true;
                break;
            }
        }
        assertFalse(isInvalidResult);

        building.getRooms().clear();
    }
}