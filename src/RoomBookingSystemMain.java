import Controller.UniversityController;
import Model.University;
import View.CliView;
import View.GuiView;

public class RoomBookingSystemMain {
    public static void main(String[] args) {
        University university = new University("BangaloreUniversity", "Bangalore");

        GuiView gui = new GuiView();

        CliView cli = new CliView();

        UniversityController controller = new UniversityController(university, gui, cli);

        Thread guiThread = new Thread(gui);
        Thread cliThread = new Thread(cli);
        guiThread.start();
        cliThread.start();
    }
}
