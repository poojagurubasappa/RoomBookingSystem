package Model;

import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.MemberNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class RoomTest {
    University uni;
    Room room;

    @Before // setup()
    public void before() throws Exception {
        uni = new University("University of St Andrews", "St Andrews");
        uni.addMember("testuser", "testuser@test.com");
        uni.addBuilding("Balfour", "DRA");
        uni.getBuildings().get("balfour").addRoom("0101");
        room = uni.getBuildings().get("balfour").getRooms().get("0101");
    }

    @org.junit.Test
    public void getName() {
        assertEquals(room.getName(), "0101");
    }

    @org.junit.Test
    public void setName() {
        room.setName("0102");
        assertEquals(room.getName(), "0102");
    }

    @org.junit.Test
    public void getBuilding() {
        Building building = room.getBuilding();
        assertEquals(building.getName(), "Balfour");
    }

    @org.junit.Test
    public void setBuilding() {
        Building building = new Building("Sellars", "DRA", uni);
        room.setBuilding(building);
        assertNotEquals("Balfour", room.getBuilding().getName());
        assertEquals(building.getName(), "Sellars");
    }

    @org.junit.Test
    public void bookRoom() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "12-12-2022", "12:00", "14:00");
        assertEquals(1, room.getBookingSchedule().size());
        assertNotNull(room.getBookingSchedule().get("12-12-2022"));
        assertEquals("testuser@test.com", room.getBookingSchedule().get("12-12-2022").get(0).getBookedBy().getEmailId());
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void bookRoom_dateCannotBeInvalid() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","12-12-1111", "12:00", "14:00");
    }

    @org.junit.Test
    public void getBookingSchedule() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "21-12-2022", "13:00", "15:00");
        HashMap<String, ArrayList<BookingDateTime>> schedules = room.getBookingSchedule();
        assertNotNull(schedules.get("21-12-2022"));
    }

    @org.junit.Test
    public void addBookingSchedule() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.addBookingSchedule("12-12-2022", "15:00", "16:00", "testuser@test.com");
        assertEquals(1, room.getBookingSchedule().size());
        assertEquals("BOOKED", room.getBookingSchedule().get("12-12-2022").get(0).getBookingStatus());

    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "12:05", "14:05");
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap_endTimeBreach() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "11:05", "12:05");
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap_startTimeBreach() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "12:15", "15:05");
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap_inBetween() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "13:05", "13:55");
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap_startTime() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "15:55");
    }

    @Test(expected = DateTimeInvalidFormat.class)
    public void addBookingSchedule_dateCannotOverlap_endTime() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com","11-12-1111", "12:00", "14:00");
        room.bookRoom("testuser@test.com","11-12-1111", "10:00", "14:00");
    }

    @org.junit.Test
    public void removeBookingInfo() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "22-12-2022", "13:00", "15:00");
        room.removeBookingInfo("testuser@test.com", "22-12-2022", "13:00", "15:00");
        assertEquals(room.getBookingSchedule().get("22-12-2022").size(), 0);
    }

    @org.junit.Test
    public void removeAllBookingsByEmail() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "24-12-2022", "13:00", "15:00");
        room.removeAllBookingsByEmail("testuser@test.com");
        assertEquals(room.getBookingSchedule().get("24-12-2022").size(), 0);
    }

    @org.junit.Test
    public void listScheduleBookedByEmail() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "25-12-2022", "13:00", "15:00");
        assertNotNull(room.listScheduleBookedByEmail("testuser@test.com"));
    }

    @org.junit.Test
    public void getAllBookingSchedule() throws DateTimeInvalidFormat, ParseException, MemberNotFoundException {
        room.bookRoom("testuser@test.com", "26-12-2022", "13:00", "15:00");
        assertTrue(room.getAllBookingSchedule().size() > 0);
    }

    @After // tearDown()
    public void after() throws Exception {
        uni=null;
    }
}