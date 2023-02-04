package View;

import Controller.UniversityController;
import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class is for the Command Line Interface for the project.
 */
public class CliView implements Runnable {
    String userInput;
    private final String Green_Padding = "\033[42m";
    private final String Green_Font = "\033[1;32m";
    private final String Red_Font = "\033[0;31m";
    private final String Purple_Font = "\033[0;35m";
    private final String Normalizer = "\033[0m";
    private final String White_Bold = "\033[1;97m";
    private final String Border_Width = "\t\t\t\t\t";
    private final String Prompt_Width = "\t\t\t\t\t\t";

    private UniversityController universityController;

    /**
     * This method is for displaying menu.
     */
    private void displayMenu() {
        String[] prompts = {
                "1. Create University Member",
                "2. Create University Building",
                "3. Create a Room",
                "4. Book a Room",
                "5. Cancel Booking",
                "6. Remove a University Member",
                "7. Remove a Building",
                "8. Remove a Room",
                "9. View Available Rooms for a Time Slot",
                "10. View Available Rooms between a Time Range",
                "11. View Booking Schedule for a Room",
                "12. View Bookings By EmailId",
                "13. Save",
                "14. Load",
                "15. Exit",
                "16. Main Menu"
        };
        System.out.print(Border_Width + Green_Font + "----------------------------------------------------------------" + Normalizer + "\n");
        System.out.print(Border_Width + Green_Padding + "Please Select An Option From Below -> Eg: Press 1 to Add a Member" + Normalizer + "\n");
        System.out.print(Border_Width + Green_Font + "----------------------------------------------------------------" + Normalizer + "\n");
        for (String prompt : prompts) {
            System.out.print(Prompt_Width + White_Bold + prompt + Normalizer + "\n");
            System.out.print(Border_Width + "----------------------------------------------------------------\n");
        }
    }

    /**
     * This method is to initiate the cli.
     */
    @Override
    public void run() {
            Scanner myScanner = new Scanner(System.in);
            this.displayMenu();
            boolean keepAlive = true;
            while (keepAlive && myScanner.hasNextLine()) {
                userInput = myScanner.nextLine();
                switch (userInput) {
                    case "1":
                        this.handleInsertPerson(myScanner);
                        break;
                    case "2":
                        this.handleInsertBuilding(myScanner);
                        break;
                    case "3":
                        this.handleCreateRoom(myScanner);
                        break;
                    case "4":
                        this.handleRoomBooking(myScanner);
                        break;
                    case "5":
                        this.handleCancelBooking(myScanner);
                        break;
                    case "6":
                        this.handleRemoveMember(myScanner);
                        break;
                    case "7":
                        this.handleRemoveBuilding(myScanner);
                        break;
                    case "8":
                        this.handleRemoveRoom(myScanner);
                        break;
                    case "9":
                        this.handleAvailableRoomsForTimeSlot(myScanner);
                        break;
                    case "10":
                        this.handleAvailableRoomsForTimeRange(myScanner);
                        break;
                    case "11":
                        handleViewBookingScheduleForRoom(myScanner);
                        break;
                    case "12":
                        handleViewBookingsByEmailId(myScanner);
                        break;
                    case "13":
                        handleSaveData();
                        break;
                    case "14":
                        handleLoadData();
                        break;
                    case "15":
                        System.out.println("Cli unavailable");
                        if(ThreadHandler.isGuiThreadClosed) {
                            System.exit(1);
                        } else {
                            ThreadHandler.isCliThreadClosed = true;
                        }
                        keepAlive = false;
                        myScanner.close();
                        break;
                    case "16":
                        this.displayMenu();
                        break;
                    default:
                        System.out.print(Red_Font + "Invalid Input, Retry" + Normalizer);
                        break;
                }
            }
        myScanner.close();
    }

    private void promptAddSuccessful() {
        System.out.println(Green_Padding + "Operation completed without errors." + Normalizer);
    }

    private void promptMenu() {
        System.out.println(Border_Width + "------------------------------------------------------ ");
        System.out.println(Border_Width + Green_Padding + "Continue with other options or press 16 for Main Menu." + Normalizer);
        System.out.println(Border_Width + "------------------------------------------------------ ");
    }

    private void handleInsertPerson(Scanner myScanner) {
        System.out.print(Green_Font + "Pressed 1 : Please enter the details to add a member to university: \n");
        System.out.println(Green_Font + "Enter the name of the Member: ");
        String name = myScanner.nextLine();
        System.out.println(Green_Font + "Enter the emailId of the Member: " + Normalizer);
        String emailId = myScanner.nextLine();
        try {
            this.universityController.insertPerson(name, emailId);
            this.promptAddSuccessful();
        } catch (EmailValidationException | NameValidationException e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        }
        this.promptMenu();
    }

    private void handleInsertBuilding(Scanner myScanner) {
        System.out.print(Green_Font + "Pressed 2 : Please enter the details to add a university building :" + Normalizer + "\n");
        System.out.println(Green_Font + "Enter the name of the Building: " + Normalizer);
        String name = myScanner.nextLine();
        System.out.println(Green_Font + "Enter the address of the Building: " + Normalizer);
        String address = myScanner.nextLine();
        try {
            this.universityController.insertBuilding(name, address);
            this.promptAddSuccessful();
        } catch (NameValidationException e) {
            System.out.println(Red_Font + e.getMessage());
        }
        this.promptMenu();
    }

    private List<String> printBuildings() {
        List<String> buildingNames = this.universityController.listBuildings();
        System.out.println(Green_Font + "Choose the Building -> Eg: Press 1 for the first option " + Normalizer);
        System.out.println(Green_Font + "------------------------------------------------------ " + Normalizer);
        int count = 1;
        for (String buildingName : buildingNames) {
            System.out.println(Prompt_Width + Purple_Font + count + " " + buildingName + Normalizer);
            count++;
        }
        return buildingNames;
    }

    private List<String> printBuildingsBookedByEmail(String email) throws MemberNotFoundException {
        List<String> buildingNames = new ArrayList<>();
        try {
            System.out.println(Green_Font + "Choose a Building -> Eg: Press 1 for the first option " + Normalizer);
            System.out.println(Green_Font + "------------------------------------------------------ " + Normalizer);
            buildingNames = this.universityController.listBuildingsBookedByEmail(email);
            int count = 1;
            for (String buildingName : buildingNames) {
                System.out.println(Prompt_Width + Purple_Font + count + " " + buildingName + Normalizer);
                count++;
            }
        } catch (Exception e) {

        }
        return buildingNames;
    }

    private List<String> printRooms(String buildingName) {
        System.out.println(Green_Font + "Choose a Room -> Eg: Press 1 for the first option " + Normalizer);
        System.out.println(Green_Font + "------------------------------------------------------ " + Normalizer);
        List<String> roomNames = this.universityController.listRooms(buildingName);
        int count = 1;
        for (String roomName : roomNames) {
            System.out.println(Prompt_Width + Purple_Font + count + " " + roomName + Normalizer);
            count++;
        }
        return roomNames;
    }

    private List<String> printRoomsBookedByEmail(String buildingName, String email) throws Exception {
        System.out.println(Green_Font + "Choose a Room -> Eg: Press 1 for the first option " + Normalizer);
        System.out.println(Green_Font + "------------------------------------------------------ " + Normalizer);
        List<String> roomNames = this.universityController.listRoomsBookedByEmail(buildingName, email);
        int count = 1;
        for (String roomName : roomNames) {
            System.out.println(Prompt_Width + Purple_Font + count + " " + roomName + Normalizer);
            count++;
        }
        return roomNames;
    }

    private List<String> printRoomSchedulesByEmail(String buildingName, String roomName, String email) throws MemberNotFoundException {
        List<String> roomSchedules = new ArrayList<>();
        try {
            System.out.println(Green_Font + "Choose a Schedule -> Eg: Press 1 for the first option " + Normalizer);
            System.out.println(Green_Font + "------------------------------------------------------ " + Normalizer);
            roomSchedules = this.universityController.listRoomSchedulesByEmail(buildingName, roomName, email);
            int count = 1;
            for (String roomSchedule : roomSchedules) {
                System.out.println(Prompt_Width + Purple_Font + count + " " + roomSchedule + Normalizer);
                count++;
            }

        } catch (Exception e) {

        }
        return roomSchedules;
    }

    private void handleCreateRoom(Scanner myScanner) {
        try {
        System.out.print(Green_Font + "Pressed 3 : Please enter the details to add a room :" + Normalizer + "\n");
        List<String> buildingNames = this.printBuildings();
        if(buildingNames.size() == 0) {
            System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
            return;
        }
        int chosenBuilding = Integer.parseInt(myScanner.nextLine());
        String buildingName = buildingNames.get(chosenBuilding - 1);
        System.out.println(Green_Font + "Enter the name of the Room: " + Normalizer);
        String roomName = myScanner.nextLine();
            this.universityController.insertRoom(buildingName, roomName);
            this.promptAddSuccessful();
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            System.out.println(Red_Font + e.getMessage());
        }
        this.promptMenu();
    }

    private void handleRoomBooking(Scanner myScanner) {
        try {
        System.out.print(Green_Font + "Pressed 4 : Please enter the details to book a room :" + Normalizer + "\n");
        List<String> buildingNames = this.printBuildings();
        if(buildingNames.size() == 0) {
            System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
            return;
        }
        int chosenBuilding = Integer.parseInt(myScanner.nextLine());
        String buildingName = buildingNames.get(chosenBuilding - 1);
        List<String> roomNames = this.printRooms(buildingName);
        if(roomNames.size() == 0) {
            System.out.println(Red_Font + "No Rooms to Select From" + Normalizer);
            return;
        }
        int chosenRoom = Integer.parseInt(myScanner.nextLine());
        String roomName = roomNames.get(chosenRoom - 1);
        System.out.println(Green_Font + "Enter your email " + Normalizer);
        String email = myScanner.nextLine();
        System.out.println(Green_Font + "Enter the date of booking: Format <dd-mm-yyyy> " + Normalizer);
        String date = myScanner.nextLine();
        System.out.println(Green_Font + "Enter the start time of booking: Format <hh:mm> " + Normalizer);
        String start = myScanner.nextLine();
        System.out.println(Green_Font + "Enter the end time of booking: Format <hh:mm> " + Normalizer);
        String end = myScanner.nextLine();
            this.universityController.bookARoom(buildingName, roomName, email, date, start, end);
            this.promptAddSuccessful();
        } catch (MemberNotFoundException | DateTimeInvalidFormat | ParseException e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            promptMenu();
        }
        this.promptMenu();
    }

    private void handleCancelBooking(Scanner myScanner) {
        System.out.print(Green_Font + "Pressed 5 : Please enter the details to cancel a booking :" + Normalizer + "\n");
        System.out.println(Green_Font + "Enter your email " + Normalizer);
        String email = myScanner.nextLine();
        List<String> buildingNames;
        try {
            buildingNames = this.printBuildingsBookedByEmail(email);
            if(buildingNames.size() == 0) {
                System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
                return;
            }
            int chosenBuilding = Integer.parseInt(myScanner.nextLine());
            String buildingName = buildingNames.get(chosenBuilding - 1);

            List<String> roomNames = this.printRoomsBookedByEmail(buildingName, email);
            if(roomNames.size() == 0) {
                System.out.println(Red_Font + "No Rooms to Select From" + Normalizer);
                return;
            }
            int chosenRoom = Integer.parseInt(myScanner.nextLine());
            String roomName = roomNames.get(chosenRoom - 1);

            List<String> roomSchedules = this.printRoomSchedulesByEmail(buildingName, roomName, email);
            int chosenSchedule = Integer.parseInt(myScanner.nextLine());
            String schedule = roomSchedules.get(chosenSchedule - 1);
            String[] extractedDateTimeInfo = schedule.split(" | ");
            String date = extractedDateTimeInfo[0];
            String start = extractedDateTimeInfo[2];
            String end = extractedDateTimeInfo[4];
            this.universityController.removeBooking(buildingName, roomName, email, date, start, end);
            this.promptAddSuccessful();
            promptMenu();

        } catch (MemberNotFoundException | ParseException e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        } catch (Exception e) {
            promptMenu();
        }
    }

    private void handleRemoveMember(Scanner myScanner) {
        System.out.println(Green_Font + "Pressed 6 : Please enter the details to remove a member" + Normalizer + "\n");
        System.out.println(Green_Font + "Enter your email " + Normalizer);
        String email = myScanner.nextLine();
        try {
            this.universityController.removeMember(email);
            this.promptAddSuccessful();
            promptMenu();
        } catch (MemberNotFoundException e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        } catch (Exception e) {
            promptMenu();
        }
    }

    private void handleRemoveBuilding(Scanner myScanner) {
        System.out.println(Green_Font + "Pressed 7 : Please enter the details to remove a building" + Normalizer + "\n");
        List<String> buildingNames;
        try {
            buildingNames = this.printBuildings();
            if(buildingNames.size() == 0) {
                System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
                return;
            }
            int chosenBuilding = Integer.parseInt(myScanner.nextLine());
            String buildingName = buildingNames.get(chosenBuilding - 1);

            this.universityController.removeBuilding(buildingName);
            this.promptAddSuccessful();
            promptMenu();
        } catch (Exception e) {
            promptMenu();
        }
    }
    private void handleRemoveRoom(Scanner myScanner) {
        try {
        System.out.println(Green_Font + "Pressed 8 : Please enter the details to remove a room" + Normalizer + "\n");

        List<String> buildingNames = this.printBuildings();
        if(buildingNames.size() == 0) {
            System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
            return;
        }
        int chosenBuilding = Integer.parseInt(myScanner.nextLine());
        String buildingName = buildingNames.get(chosenBuilding - 1);
        List<String> roomNames = this.printRooms(buildingName);
        if(roomNames.size() == 0) {
            System.out.println(Red_Font + "No Rooms to Select From" + Normalizer);
            return;
        }
        int chosenRoom = Integer.parseInt(myScanner.nextLine());
        String roomName = roomNames.get(chosenRoom - 1);

            this.universityController.removeRoom(buildingName, roomName);
            this.promptAddSuccessful();
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            promptMenu();
        }
        this.promptMenu();
    }

    private void handleAvailableRoomsForTimeSlot(Scanner myScanner) {
        System.out.println(Green_Font + "Pressed 9 : Please enter the details to view rooms for an input time" + Normalizer + "\n");
        System.out.println(Green_Font + "Enter a date to view available rooms: Eg: <dd-mm-yyyy> " + Normalizer);
        String date = myScanner.nextLine();
        System.out.println(Green_Font + "Enter a start time to view available rooms: Eg: <hh:mm> " + Normalizer);
        String time = myScanner.nextLine();
        try {
            HashMap<String, ArrayList<String>> availableRoomsByBuilding = this.universityController.getAvailableRoomsForStartTime(date, time);
            for (String building : availableRoomsByBuilding.keySet()) {
                System.out.println("Building: " + building);
                System.out.println("Rooms: ");
                if (building != null) {
                    for (String roomName : availableRoomsByBuilding.get(building)) {
                        System.out.println(roomName);
                    }
                }
                System.out.println("---------------------------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
            this.promptMenu();
        }
        promptMenu();
    }

    private void handleAvailableRoomsForTimeRange(Scanner myScanner) {
        System.out.println(Green_Font + "Pressed 10 : Please enter the details to view rooms for an input time range" + Normalizer  + "\n");
        System.out.println(Green_Font + "Enter a date to view available rooms: Eg: <dd-mm-yyyy> " + Normalizer);
        String date = myScanner.nextLine();
        System.out.println(Green_Font + "Enter a start time to view available rooms: Eg: <hh:mm> " + Normalizer);
        String start = myScanner.nextLine();
        System.out.println(Green_Font + "Enter an end time to view available rooms: Eg: <hh:mm> " + Normalizer);
        String end = myScanner.nextLine();
        HashMap<String, ArrayList<String>> availableRoomsByBuilding;
        try {
            availableRoomsByBuilding = this.universityController.getAvailableRoomsForTimeRange(date, start, end);
            for (String building : availableRoomsByBuilding.keySet()) {
                System.out.println("Building: " + building);
                System.out.println("Rooms: ");
                if (building != null) {
                    for (String roomName : availableRoomsByBuilding.get(building)) {
                        System.out.println(roomName);
                    }
                }
                System.out.println("---------------------------------------------------------------------------------------------");
            }
            promptMenu();
        } catch (Exception e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
            promptMenu();
        }
    }

    private void handleViewBookingScheduleForRoom(Scanner myScanner) {
        try {
            System.out.println(Green_Font + "Pressed 11 : Please enter the details to list schedules for a room : " + "\n");
            List<String> buildingNames = this.printBuildings();
            if(buildingNames.size() == 0) {
                System.out.println(Red_Font + "No Buildings to Select From" + Normalizer);
                return;
            }
            int chosenBuilding = Integer.parseInt(myScanner.nextLine());
            String buildingName = buildingNames.get(chosenBuilding - 1);

            List<String> roomNames = this.printRooms(buildingName);
            if(roomNames.size() == 0) {
                System.out.println(Red_Font + "No Rooms to Select From" + Normalizer);
                return;
            }
            int chosenRoom = Integer.parseInt(myScanner.nextLine());
            String roomName = roomNames.get(chosenRoom - 1);
            for (String schedule : universityController.getRoomBookingSchedule(buildingName, roomName)) {
                System.out.println(schedule);
            }
            promptMenu();
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        }
    }

    private void handleViewBookingsByEmailId(Scanner myScanner) {
        System.out.println(Green_Font + "Pressed 12 : Please enter the details to view your bookings : " + "\n");
        System.out.println(Green_Font + "Enter your email " + Normalizer);
        String email = myScanner.nextLine();
        try {
            List<String> bookings = universityController.listAllRoomsBookedByEmail(email);
            for (String booking : bookings) {
                System.out.println(booking);
            }
        } catch (MemberNotFoundException e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        } catch (Exception e) {
            System.out.println(Red_Font + e.getMessage() + Normalizer);
        }
        promptMenu();
    }

    private void handleSaveData() {
        System.out.println(Green_Font + "Pressed 13 : Saving Data ..." + "\n");
        universityController.saveAllData();
        System.out.println(Green_Font + "Data Saved Successfully" + Normalizer);
    }

    private void handleLoadData() {
        System.out.println(Green_Font + "Pressed 14 : Loading Data...." + "\n");
        universityController.loadAllData();
        System.out.println(Green_Font + "Data Loaded Successfully" + Normalizer);
    }

    /**
     * This sets the controller to the constructor.
     * @param universityController is the controller.
     */
    public void setController(UniversityController universityController) {
        this.universityController = universityController;
    }
}
