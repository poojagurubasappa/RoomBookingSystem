package Model;

import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class UniversityTest {
    University uni = new University("University of St Andrews", "St Andrews");
    @Test
    public void getBuildings() {
        Building b1 = new Building("Balfour", "DRA", uni);
        Building b2 = new Building("Tansen", "DRA", uni);
        HashMap<String, Building> uniBuildings = new HashMap<>();
        uniBuildings.put("Balfour", b1);
        uniBuildings.put("Tansen", b2);
        uni.setBuildings(uniBuildings);

        assertEquals(uniBuildings.size(), uni.getBuildings().size());

        uni.getBuildings().clear();
        assertEquals(0, uni.getBuildings().size());
    }

    @Test
    public void setBuildings() {
        HashMap<String, Building> uniBuildings = new HashMap<>();
        String buildingName = "Balfour";
        Building b1 = new Building(buildingName, "DRA", uni);
        uniBuildings.put("Balfour", b1);

        uni.setBuildings(uniBuildings);
        assertEquals(1, uni.getBuildings().size());
        assertTrue(uniBuildings.containsKey("Balfour"));
        uni.getBuildings().clear();
    }

    @Test
    public void listBuildingNames() throws NameValidationException {
        String buildingName1 = "Balfour";
        String buildingName2 = "Russells";

        uni.addBuilding(buildingName1, "DRA");
        uni.addBuilding(buildingName2, "DRA");

        List<String> uniBuildingNames = uni.listBuildingNames();

        assertTrue(uniBuildingNames.contains(buildingName1));
        assertTrue(uniBuildingNames.contains(buildingName2));
    }

    @Test
    public void listBuildingsBookedByEmail() throws Exception {
        String userName = "testUser";
        String email = "test@test.com";
        String buildingName = "balfour";
        String address = "dra";
        String roomName = "0101";

        uni.addMember(userName, email);
        uni.addBuilding(buildingName, address);
        uni.getBuildings().get(buildingName).addRoom(roomName);
        uni.getBuildings().get(buildingName).getRooms().get(roomName).addBookingSchedule("12-12-2022", "09:20", "09:30", email);
        List<String> buildingsBooked = uni.listBuildingsBookedByEmail(email);

        assertEquals(buildingsBooked.get(0), buildingName);

        uni.getBuildings().clear();
    }

    @Test
    public void getMembers() {
        assertEquals(0, uni.getMembers().size());
        HashMap<String, UniversityMember> members = new HashMap<>();
        UniversityMember member = new UniversityMember("testUser", "test@test.com", uni);
        members.put("test@test.com", member);
        uni.setMembers(members);

        assertEquals(1, uni.getMembers().size());
        assertEquals(uni.getMembers().get("test@test.com").getName(), "testUser");

        uni.getMembers().clear();
        assertEquals(0, uni.getMembers().size());
    }

    @Test
    public void setMembers() {
        HashMap<String, UniversityMember> members = new HashMap<>();
        UniversityMember member = new UniversityMember("testUser", "test@test.com", uni);
        members.put("test@test.com", member);

        uni.setMembers(members);
        assertEquals(1, uni.getMembers().size());

        uni.getMembers().clear();
    }

    @Test
    public void getName() {
        assertEquals("University of St Andrews", uni.getName());
    }

    @Test
    public void setName() {
        uni.setName("University of STA");
        assertEquals("University of STA", uni.getName());
    }

    @Test
    public void getAddress() {
        assertEquals("St Andrews", uni.getAddress());
    }

    @Test
    public void setAddress() {
        uni.setAddress("Dundee");
        assertEquals("Dundee", uni.getAddress());
    }

    @Test
    public void addBuilding() throws NameValidationException {
        String buildingName = "Sellars";
        uni.addBuilding(buildingName, "DRA, St Andrews");
        assertNotNull(uni.getBuildings());
        assertEquals(1, uni.getBuildings().size());
        assertTrue(uni.getBuildings().containsKey(buildingName.toLowerCase()));

        uni.removeBuilding("Sellars");
    }

    @Test(expected = NameValidationException.class)
    public void addBuilding_nameCannotBeDuplicate() throws NameValidationException, EmailValidationException {
        String buildingName = "Sellars";
        uni.addBuilding(buildingName, "DRA, St Andrews");
        uni.addBuilding(buildingName, "DRA, St Andrews");

        uni.removeBuilding("Sellars");
    }

    @Test
    public void removeBuilding() throws NameValidationException {
        String buildingName = "Sellars";
        uni.addBuilding(buildingName, "DRA, St Andrews");

        uni.removeBuilding("Sellars");
        assertEquals(0, uni.getBuildings().size());
    }

    @Test
    public void addMember() throws MemberNotFoundException, EmailValidationException, NameValidationException {
        String name = "testUser";
        String email = "testUser@test.com";
        uni.addMember(name, email);
        assertNotNull(uni.getMembers());
        assertEquals(1, uni.getMembers().size());
        assertTrue(uni.getMembers().containsKey(email.toLowerCase()));

        uni.removeMember("testUser@test.com");
    }

    @Test(expected = EmailValidationException.class)
    public void addMember_emailCannotBeDuplicate() throws MemberNotFoundException, NameValidationException, EmailValidationException {
        String name = "testUser";
        String email = "testUser@test.com";
        uni.addMember(name, email);
        uni.addMember(name, email);

        uni.removeMember("testUser@test.com");
    }

    @Test(expected = EmailValidationException.class)
    public void addMember_emailCannotBeInvalid() throws NameValidationException, EmailValidationException {
        String name = "testUser";
        String email = "testUser";
        uni.addMember(name, email);
        uni.addMember(name, email);
    }


    @Test
    public void removeMember() throws EmailValidationException, MemberNotFoundException, NameValidationException {
        String name = "testUser";
        String email = "testUser@test.com";
        uni.addMember(name, email);

        uni.removeMember(email);
        assertEquals(0, uni.getMembers().size());
    }
}