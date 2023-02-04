package Model;

import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a University.
 */
public class University implements Serializable {
    private final InputValidator inputValidator;
    private HashMap<String, Building> buildings;
    private HashMap<String, UniversityMember> members;
    private String name;
    private String address;

    /**
     * This is the class constructor for the University.
     *
     * @param name    is the name of the University.
     * @param address is the address of the University.
     */
    public University(String name, String address) {
        this.name = name;
        this.address = address;
        this.buildings = new HashMap<>();
        this.members = new HashMap<>();
        this.inputValidator = new InputValidator();
    }

    /**
     * This method is to retrieve all the buildings affiliated to the University.
     *
     * @return all the buildings affiliated to the University.
     */
    public HashMap<String, Building> getBuildings() {
        return buildings;
    }

    /**
     * This method assigns all the buildings affiliated to the University.
     */
    public void setBuildings(HashMap<String, Building> buildings) {
        this.buildings = buildings;
    }

    public List<String> listBuildingNames() {
        List<String> buildingNames = new ArrayList<>();
        for (Building b : this.getBuildings().values()) {
            buildingNames.add(b.getName());
        }
        return buildingNames;
    }

    public List<String> listBuildingsBookedByEmail(String emailId) throws MemberNotFoundException {
        List<String> buildingNames = new ArrayList<>();
        for (Building b : this.getBuildings().values()) {
            for (Room r : b.getRooms().values()) {
                if (r.listScheduleBookedByEmail(emailId) != null) {
                    buildingNames.add(b.getName());
                    break;
                }
            }
        }
        return buildingNames;
    }

    /**
     * This method is to retrieve all the members registered with the University.
     *
     * @return a map of University members that can be looked up by emailId.
     */
    public HashMap<String, UniversityMember> getMembers() {
        return members;
    }

    /**
     * This method assigns all the members registered with the University to a hash map.
     */
    public void setMembers(HashMap<String, UniversityMember> members) {
        this.members = members;
    }

    /**
     * This method is to retrieve the name of the University.
     *
     * @return name of the University.
     */
    public String getName() {
        return name;
    }

    /**
     * This method is for assigning the name of the University.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is to retrieve the address of the University.
     *
     * @return the address of the University.
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method assigns the address of the University.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method adds a building to the list of buildings affiliated with the University.
     */
    public void addBuilding(String buildingName, String address) throws NameValidationException {
        if (inputValidator.isEmptyString(address)) {
            throw new NameValidationException("Address cannot be empty");
        }
        String name = buildingName.toLowerCase();
        if (inputValidator.isEmptyString(buildingName) || this.buildings.get(name) != null) {
            throw new NameValidationException("This building name is already taken or empty. Please choose a different name");
        }
        Building building = new Building(buildingName, address, this);
        this.buildings.put(name, building);
    }

    /**
     * This method removes a building from the list of buildings affiliated with the University.
     */
    public void removeBuilding(String buildingName) {
        String name = buildingName.toLowerCase();
        if (this.buildings.get(name) != null) {
            this.buildings.remove(name);
        }
    }

    /**
     * This method creates a member and adds to the list of registered members of the University.
     */
    public void addMember(String name, String emailId) throws EmailValidationException, NameValidationException {
        if (inputValidator.isEmptyString(name)) {
            throw new NameValidationException("Member name is invalid");
        }
        if (!inputValidator.isValidEmailFormat(emailId)) {
            throw new EmailValidationException("Invalid Email Format");
        }
        String emailLowerCase = emailId.toLowerCase();
        if (this.members.get(emailLowerCase) != null) {
            throw new EmailValidationException("Email already Registered");
        }
        UniversityMember member = new UniversityMember(name, emailId, this);
        this.members.put(emailLowerCase, member);
    }

    /**
     * This method removes a member from the list of registered members of the University.
     */
    public void removeMember(String emailId) throws MemberNotFoundException {
        String email = emailId.toLowerCase();
        UniversityMember member = this.members.get(email);
        if (member != null) {
            member.removeAllBookedRooms();
            this.members.remove(email);
        }
    }
}
