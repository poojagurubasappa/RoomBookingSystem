package View;

import Controller.UniversityController;
import RoomBookingSystemException.DateTimeInvalidFormat;
import RoomBookingSystemException.EmailValidationException;
import RoomBookingSystemException.MemberNotFoundException;
import RoomBookingSystemException.NameValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * This is the class for Graphical User Interface for the project.
 */
public class GuiView implements PropertyChangeListener, ActionListener, Runnable, WindowListener {
    private JPanel panel;

    private JButton addMemberButton;
    private JButton addBuildingButton;
    private JButton addRoomButton;
    private JButton bookRoomButton;
    private JButton cancelBookingButton;
    private JButton removeMemberButton;
    private JButton removeBuildingButton;
    private JButton removeRoomButton;
    private JButton listAvailableRoomsButton;
    private JButton viewBookingsForRoomButton;

    private JButton viewBookingsByEmailIdButton;
    private JButton saveButton;
    private JButton loadButton;
    private UniversityController universityController;

    /**
     * This method is to initiate the GUI.
     */
    @Override
    public void run() {
        JFrame frame = new JFrame("Room Booking System"); //The outer window for GUI
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panel = new JPanel(); //The layout

        panel.setLayout(new GridLayout(15, 1));

        frame.add(panel, BorderLayout.CENTER);
        addHeaderElement();
        addControlElements();
        addActionListenerForButtons(this);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(this);

        frame.setVisible(true);
    }

    @Override
    public void windowClosed(WindowEvent e){
        if (ThreadHandler.isCliThreadClosed) {
            System.exit(1);
        } else {
            ThreadHandler.isGuiThreadClosed = true;
        }
    }

    private void addHeaderElement() {
        JLabel headerLabel = new JLabel("Welcome to Room Booking System", SwingConstants.CENTER);
        JLabel subHeader = new JLabel("Please select an option from here to proceed", SwingConstants.CENTER);
        panel.add(headerLabel);
        panel.add(subHeader);
    }

    private void addControlElements() {
        addMemberButton = new JButton("Add a Member");
        addBuildingButton = new JButton("Add a Building");
        addRoomButton = new JButton("Add a Room");
        bookRoomButton = new JButton("Book a Room");
        cancelBookingButton = new JButton("Cancel a Booking");
        removeMemberButton = new JButton("Remove a Member");
        removeBuildingButton = new JButton("Remove a Building");
        removeRoomButton = new JButton("Remove a Room");
        listAvailableRoomsButton = new JButton("List Available Rooms");
        viewBookingsForRoomButton = new JButton("View Booking Schedule");
        viewBookingsByEmailIdButton = new JButton("View Bookings by EmailId");
        saveButton = new JButton("Save all data");
        loadButton = new JButton("Load all data");
        panel.add(addMemberButton);
        panel.add(addBuildingButton);
        panel.add(addRoomButton);
        panel.add(bookRoomButton);
        panel.add(cancelBookingButton);
        panel.add(removeMemberButton);
        panel.add(removeBuildingButton);
        panel.add(removeRoomButton);
        panel.add(listAvailableRoomsButton);
        panel.add(viewBookingsForRoomButton);
        panel.add(viewBookingsByEmailIdButton);
        panel.add(saveButton);
        panel.add(loadButton);
    }

    public void addActionListenerForButtons(ActionListener al) {
        addMemberButton.addActionListener(al);
        addBuildingButton.addActionListener(al);
        addRoomButton.addActionListener(al);
        bookRoomButton.addActionListener(al);
        cancelBookingButton.addActionListener(al);
        removeMemberButton.addActionListener(al);
        removeBuildingButton.addActionListener(al);
        removeRoomButton.addActionListener(al);
        listAvailableRoomsButton.addActionListener(al);
        viewBookingsForRoomButton.addActionListener(al);
        viewBookingsByEmailIdButton.addActionListener(al);
        saveButton.addActionListener(al);
        loadButton.addActionListener(al);
    }

    /**
     * This method sets the constructor field with controller.
     * @param universityController is the controller.
     */
    public void setController(UniversityController universityController) {
        this.universityController = universityController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (addMemberButton.equals(source)) {
            handleAddMember();
        } else if (addBuildingButton.equals(source)) {
            handleAddBuilding();
        } else if (addRoomButton.equals(source)) {
            handleAddRoom();
        } else if (bookRoomButton.equals(source)) {
            handleBookRoom();
        } else if (cancelBookingButton.equals(source)) {
            handleCancelBooking();
        } else if (removeMemberButton.equals(source)) {
            handleRemoveMember();
        } else if (removeBuildingButton.equals(source)) {
            handleRemoveBuilding();
        } else if (removeRoomButton.equals(source)) {
            handleRemoveRoom();
        } else if (listAvailableRoomsButton.equals(source)) {
            handleListAvailableRooms();
        } else if (viewBookingsForRoomButton.equals(source)) {
            handleViewBookingsForRoom();
        } else if (saveButton.equals(source)) {
            saveData();
        } else if (loadButton.equals(source)) {
            loadData();
        } else if (viewBookingsByEmailIdButton.equals(source)) {
            handleViewBookingsByEmailId();
        }
    }

    private void handleAddMember() {
        try {
            String name = JOptionPane.showInputDialog("Enter the name here");
            if (name != null) {
                String email = JOptionPane.showInputDialog("Enter your email here");
                this.universityController.insertPerson(name, email);
                JOptionPane.showMessageDialog(null, "Member registered successfully");
            }
        } catch (EmailValidationException | NameValidationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void loadData() {
        this.universityController.loadAllData();
        JOptionPane.showMessageDialog(null, "Data loaded successfully");
    }

    private void saveData() {
        this.universityController.saveAllData();
        JOptionPane.showMessageDialog(null, "Data saved successfully");
    }

    private void handleAddBuilding() {
        String buildingName = JOptionPane.showInputDialog("Enter Building name here");
        if (buildingName != null) {
            String address = JOptionPane.showInputDialog("Enter Building's address here");
            if (address != null) {
                try {
                    this.universityController.insertBuilding(buildingName, address);
                    JOptionPane.showMessageDialog(null, "Building add is successful");
                } catch (NameValidationException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }

    }

    private void handleAddRoom() {
        try {
            List<String> buildingsList = universityController.listBuildings();
            if (buildingsList.size() == 0) {
                JOptionPane.showMessageDialog(null, "No Buildings Found To Add Room");
                return;
            }
            String[] buildings = buildingsList.toArray(new String[0]);
            Object buildingName = JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.QUESTION_MESSAGE, null, buildings, buildings[0]);
            if (buildingName != null) {
                String roomName = JOptionPane.showInputDialog("Enter Room name here");
                if (roomName != null) {
                    this.universityController.insertRoom((String) buildingName, roomName);
                    JOptionPane.showMessageDialog(null, "Room add is successful");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleBookRoom() {
        try {
            List<String> buildingsList = universityController.listBuildings();
            if (buildingsList.size() == 0) {
                JOptionPane.showMessageDialog(null, "No Buildings Found To Add Room");
                return;
            }
            String[] buildings = buildingsList.toArray(new String[0]);
            Object buildingName = JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.PLAIN_MESSAGE, null, buildings, buildings[0]);
            if (buildingName != null) {
                List<String> roomNamesList = this.universityController.listRooms((String) buildingName);
                if (roomNamesList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No Rooms Found");
                    return;
                }
                String[] roomNames = roomNamesList.toArray(new String[0]);
                Object roomName = JOptionPane.showInputDialog(null, "Select Room name here", "Rooms", JOptionPane.QUESTION_MESSAGE, null, roomNames, roomNames[0]);
                if (roomName != null) {
                    String email = JOptionPane.showInputDialog("Enter your email here");
                    if (email != null) {
                        String date = JOptionPane.showInputDialog("Enter the date of booking here : Format <dd-mm-yyyy>");
                        if (date != null) {
                            String start = JOptionPane.showInputDialog("Enter the start time of booking here : Format <hh:mm>");
                            if (start != null) {
                                String end = JOptionPane.showInputDialog("Enter the end time of booking here : Format <hh:mm>");
                                if (end != null) {
                                    this.universityController.bookARoom((String) buildingName, (String) roomName, email, date, start, end);
                                    JOptionPane.showMessageDialog(null, "Booking Successful");
                                }
                            }
                        }
                    }
                }
            }
        } catch (MemberNotFoundException | DateTimeInvalidFormat | ParseException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleCancelBooking() {
        String email = JOptionPane.showInputDialog("Enter your email here");
        try {
            if (email != null) {
                List<String> buildingsList = universityController.listBuildingsBookedByEmail(email);
                if (buildingsList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No Buildings Found To Cancel Booking");
                    return;
                }
                String[] buildings = buildingsList.toArray(new String[0]);
                String buildingName = (String) JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.PLAIN_MESSAGE, null, buildings, buildings[0]);
                if (buildingName != null) {
                    List<String> roomNamesList = this.universityController.listRoomsBookedByEmail(buildingName, email);
                    if (roomNamesList.size() == 0) {
                        JOptionPane.showMessageDialog(null, "No Rooms Found To Cancel Booking");
                        return;
                    }
                    String[] rooms = roomNamesList.toArray(new String[0]);
                    String roomName = (String) JOptionPane.showInputDialog(null, "Select Room name here", "Rooms", JOptionPane.PLAIN_MESSAGE, null, rooms, rooms[0]);
                    if (roomName != null) {
                        List<String> roomSchedules = this.universityController.listRoomSchedulesByEmail(buildingName, roomName, email);
                        if (roomSchedules.size() == 0) {
                            JOptionPane.showMessageDialog(null, "No Schedules Found To Cancel Booking");
                            return;
                        }
                        String[] schedules = roomSchedules.toArray(new String[0]);
                        String schedule = (String) JOptionPane.showInputDialog(null, "Select a schedule to cancel", "Schedules", JOptionPane.PLAIN_MESSAGE, null, schedules, schedules[0]);
                        if (schedule != null) {
                            String[] extractedDateTimeInfo = schedule.split(" | ");
                            String date = extractedDateTimeInfo[0];
                            String start = extractedDateTimeInfo[2];
                            String end = extractedDateTimeInfo[4];
                            this.universityController.removeBooking(buildingName, roomName, email, date, start, end);
                            JOptionPane.showMessageDialog(null, "Cancellation Successful");
                        }

                    }

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleRemoveMember() {
        try {
            String email = JOptionPane.showInputDialog("Enter your email here");
            if (email != null) {
                this.universityController.removeMember(email);
                JOptionPane.showMessageDialog(null, "Member removed successfully.");
            }
        } catch (MemberNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Member not registered with the University.");
        }
    }

    private void handleRemoveBuilding() {
        List<String> buildingsList = universityController.listBuildings();
        if (buildingsList.size() == 0) {
            JOptionPane.showMessageDialog(null, "No Buildings Found To Remove Building");
            return;
        }
        String[] buildings = buildingsList.toArray(new String[0]);
        String buildingName = (String) JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.PLAIN_MESSAGE, null, buildings, buildings[0]);
        this.universityController.removeBuilding(buildingName);
        JOptionPane.showMessageDialog(null, "Building removed successfully.");
    }

    private void handleRemoveRoom() {
        try {
            List<String> buildingsList = this.universityController.listBuildings();
            if (buildingsList.size() == 0) {
                JOptionPane.showMessageDialog(null, "No Buildings Found To Remove Room From");
                return;
            }
            String[] buildings = buildingsList.toArray(new String[0]);
            String buildingName = (String) JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.PLAIN_MESSAGE, null, buildings, buildings[0]);
            if (buildingName != null) {
                List<String> roomNamesList = this.universityController.listRooms(buildingName);
                if (roomNamesList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No Rooms Found To Remove");
                }
                String[] rooms = roomNamesList.toArray(new String[0]);
                String roomName = (String) JOptionPane.showInputDialog(null, "Select Room name here", "Rooms", JOptionPane.PLAIN_MESSAGE, null, rooms, rooms[0]);
                if (roomName != null) {
                    this.universityController.removeRoom(buildingName, roomName);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    private void handleListAvailableRooms() {
        try {
            String[] options = new String[2];
            options[0] = "View Available Rooms for a time slot";
            options[1] = "View Available Rooms for a Time Range";
            String option = (String) JOptionPane.showInputDialog(null, "Select option here", "Options", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (option != null) {
                String date = JOptionPane.showInputDialog("Enter the date here : Format <dd-mm-yyyy>");
                String start = JOptionPane.showInputDialog("Enter the start time here : Format <hh:mm>");
                String end;
                HashMap<String, ArrayList<String>> availableRoomsByBuilding;
                if(option.equals(options[1])) {
                    end = JOptionPane.showInputDialog("Enter the end time here : Format <hh:mm>");
                    availableRoomsByBuilding = this.universityController.getAvailableRoomsForTimeRange(date, start, end);
                } else {
                    availableRoomsByBuilding = this.universityController.getAvailableRoomsForStartTime(date, start);
                }
                    ArrayList<String> results = new ArrayList<>();
                    for (String building : availableRoomsByBuilding.keySet()) {
                        if (building != null) {
                            for (String roomName : availableRoomsByBuilding.get(building)) {
                                results.add(building + ": " + roomName);
                            }
                        }
                    }
                    String[] resArr = results.toArray(new String[0]);
                    String.join("\n", resArr);
                    JOptionPane.showMessageDialog(null, resArr);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleViewBookingsForRoom() {
        try {
            List<String> buildingsList = this.universityController.listBuildings();
            if (buildingsList.size() == 0) {
                JOptionPane.showMessageDialog(null, "No Buildings Found To View Bookings");
                return;
            }
            String[] buildings = buildingsList.toArray(new String[0]);
            String buildingName = (String) JOptionPane.showInputDialog(null, "Select Building name here", "Buildings", JOptionPane.PLAIN_MESSAGE, null, buildings, buildings[0]);
            if (buildingName != null) {
                List<String> roomNamesList = this.universityController.listRooms(buildingName);
                if (roomNamesList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No Rooms Found To Remove");
                    return;
                }
                String[] rooms = roomNamesList.toArray(new String[0]);
                String roomName = (String) JOptionPane.showInputDialog(null, "Select Room name here", "Rooms", JOptionPane.PLAIN_MESSAGE, null, rooms, rooms[0]);
                String[] sch = universityController.getRoomBookingSchedule(buildingName, roomName).toArray(new String[0]);
                String schedule = String.join("\n", sch);
                if(schedule.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "No Bookings here. Free to book");
                    return;
                }
                JOptionPane.showMessageDialog(null, schedule);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void handleViewBookingsByEmailId() {
        try {
            String email = JOptionPane.showInputDialog("Enter your email here");
            if(email != null) {
                String[] bookings = universityController.listAllRoomsBookedByEmail(email).toArray(new String[0]);
                String bkngs = String.join("\n", bookings);
                JOptionPane.showMessageDialog(null, bkngs);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
