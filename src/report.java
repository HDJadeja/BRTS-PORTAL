import java.util.*;
import java.io.*;
import java.sql.*;

class report {

    Connection con;

    report(Connection con) {
        this.con = con;
    }

    void generate() throws Exception {

        ResultSet rs = con.createStatement().executeQuery("select current_time");
        rs.next();
        System.out.println("time is " + rs.getString(1));
        String[] timee = rs.getString(1).split(":");

        // main line// String cts = timee[0] + timee[1];

        String cts = timee[0] + timee[1]; // hour and minute
        int ct = Integer.parseInt(cts);

        if (ct >= 1100) {
            // means all station are closed and now report can be generated
            File f = new File("REPORT.txt");
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            ResultSet r = con.createStatement().executeQuery("select sum(total_revenue) from station_details");
            r.next();
            int totalrevenue = r.getInt(1);

            ResultSet r1 = con.createStatement().executeQuery("select station_name,total_revenue from station_details");

            bw.write("==== STATION NAME =========== TOTAL REVENUE(station wise) ===========");
            bw.newLine();
            while (r1.next()) {
                bw.write("=================================================================================");
                bw.newLine();
                bw.write("   " + r1.getString(1) + "     " + r1.getInt(2) + " RS ");
                bw.newLine();
                bw.write("=================================================================================");
                bw.newLine();
            }
            bw.write("======================================================================");
            bw.newLine();
            bw.write("==== TOTAL REVENUE OF DAY => " + totalrevenue + " RS");
            bw.newLine();

            ResultSet r2 = con.createStatement()
                    .executeQuery("select bus_number from bus_details where status = 'BREAKDOWN'");

            int totalbreakdown = 0;

            bw.write("=============================== BREAKDOWN INFO ========================");
            bw.newLine();
            bw.write("==> LIST OF BUS :: ");
            while (r2.next()) {
                totalbreakdown++;
                bw.write(r2.getString(1) + " , ");
            }
            bw.newLine();
            bw.write("==> TOTAL BREAKDOWN :: " + totalbreakdown);
            bw.newLine();
            bw.write("=========================================================================");
            bw.newLine();

            ResultSet r3 = con.createStatement()
                    .executeQuery("select busno,delay_time from bus_schedule where delay_time is not null");
            int totaldelayedbus = 0;

            bw.write("============================ DELAYED BUS INFO ========================");
            bw.newLine();
            bw.write("==> BUS NUMBER  ::  DELAYED TIME  ");
            bw.newLine();
            while (r3.next()) {
                totaldelayedbus++;
                bw.write("==> " + r3.getString(1) + " :: " + r3.getTime(2));
                bw.newLine();
            }
            bw.newLine();
            bw.write("==> TOTAL DELAYED BUS :: " + totaldelayedbus);
            bw.newLine();
            bw.write("=========================================================================");
            bw.newLine();
            bw.close();
        } else {
            System.out.println("\n ============================================================");
            System.out.println(" REPORT CANT BE GENRATED AS ITS NOT STATION CLOSING TIME YET |");
            System.out.println(" ============================================================");
        }

    }

}
