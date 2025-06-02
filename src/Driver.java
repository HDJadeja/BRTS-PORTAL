import java.util.*;
import java.io.*;
import java.sql.*;

public class Driver extends Thread {

    Scanner hj = new Scanner(System.in);

    String busno;
    Connection con;
    String source;
    String desti;
    brts objbrts;
    ArrayList<brts.station> al;

    Driver(Connection con, brts objbrts) {
        this.con = con;
        this.objbrts = objbrts;
    }

    void login(String id) throws Exception {
        PreparedStatement pst1 = con.prepareStatement("select * from driver_details where did = ?");
        pst1.setString(1, id);
        ResultSet rs = pst1.executeQuery();
        if (!rs.next()) {
            System.out.println(" INVALID DRIVER ID ");
        } else {
            System.out.println(" LOG IN SUCCSFULLY \n");

            System.out.print("=> ENTER BUS NUMBER :: ");
            busno = hj.nextLine();
            CallableStatement cst1 = con.prepareCall("{ call RUNNINGbus(?,?)}");
            cst1.setString(1, id);
            cst1.setString(2, busno);
            cst1.execute();
            startBus();
        }
    }

    void startBus() throws Exception {

        PreparedStatement pst = con
                .prepareStatement(" select source,destination from bus_details where bus_number = ?");
        pst.setString(1, busno);
        ResultSet rs = pst.executeQuery();

        String query = "UPDATE bus_schedule SET stime = CURRENT_TIME,delay_time = TIMEDIFF(CURRENT_TIME,sstime) WHERE busno = ? AND eetime>CURRENT_TIME AND sstime BETWEEN CURRENT_TIME-INTERVAL 1 HOUR AND CURRENT_TIME";
        PreparedStatement pst2 = con.prepareStatement(query);
        pst2.setString(1, busno);
        int isbusscheduled = pst2.executeUpdate();
        // System.out.println(isbusscheduled + " is value");

        if (!rs.next()) {
            System.out.println(" BUS NOT FOUND ");
        } else if (isbusscheduled == 0) {
            System.out.println(" BUS NOT SCHEDULED ");
        } else {
            source = rs.getString("source");
            desti = rs.getString("destination");
            al = objbrts.findroute(source, desti);

            start();
            join();
        }
    }

    public void run() {
        System.out.println(" ====== WELCOME TO BRTS ======");
        for (brts.station obj : al) {
            try {
                System.out.println(" NEXT BRTS STATION :: " + obj.sname
                        + "/n ===================================================");
                Thread.sleep(9000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(" THIS BRTS STATION :: " + obj.sname);
        }
    }
}
