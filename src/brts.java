// dsa project 

// BRTS 
// day 1 :: making hallow structure 

import java.util.*;
import java.io.*;
import java.sql.*;

class brts {

    class station {
        String sname;
        station nextl;
        station nextr;
        station prev;
        String code;
        double prevdistance;

        public station() {
            nextl = null;
            nextr = null;
        }

        public String getsname() {
            return sname;
        }
    }

    station mainstation; // works as head
    boolean flag = false;
    station found; // found e node che jeh node uper apde add mate prev station act kerse

    Scanner hj = new Scanner(System.in);

    void addstation(String stationname, String prevstationname, double distance) throws SQLException {

        station stationnode = new station();

        stationnode.sname = stationname;
        stationnode.prevdistance = distance;

        if (mainstation == null) // ager head empty toh
        {
            mainstation = stationnode;
            mainstation.code = "0";
            CallableStatement addstationdb = con.prepareCall("{ call stationADD(?,?,?,?,?,?)}");
            addstationdb.setString(1, stationname);
            addstationdb.setString(2, stationnode.code);
            addstationdb.setInt(3, 0);
            addstationdb.setString(4, "NONE");
            addstationdb.setString(5, null);
            addstationdb.setString(6, null);
            addstationdb.executeUpdate();
        }

        else if (mainstation != null) {
            // next bhi puchvanu che

            String prevsname = prevstationname;

            System.out.println();

            if (prevsname != "NULL") {

                station tempstation = mainstation;

                traverse(mainstation, prevsname);

                if (flag) {

                    if (found.nextl != null && found.nextr != null) {
                        System.out.println(found.nextr.sname);
                        System.out.println(found.nextl.sname);
                        System.out.println(" cant add station cuz prev station is pointing two statoion already ");
                    }

                    else {

                        stationnode.prev = found;

                        // cheking that jeh station che e leaf che , ager nathi to stationnode nu next
                        // set kervu pade

                        if (found.nextl == null) {
                            found.nextl = stationnode;
                            stationnode.code = found.code + "1";
                            // System.out.println(" now " + found.sname + " nu left is " +
                            // found.nextl.sname);
                            // System.out.println(stationnode.code);

                            // have e khaber padi k jeh station e temp ubho che e station e koi child nathi
                            // cuz ager left null toh right bhi null hase
                        }

                        else if (found.nextr == null) {
                            found.nextr = stationnode;
                            stationnode.code = found.code + "2";
                            // System.out.println(" now " + found.sname + " nu right is " +
                            // found.nextr.sname);
                            // System.out.println(stationnode.code);
                        }

                        CallableStatement addstationdb = con.prepareCall("{ call stationADD(?,?,?,?,?,?)}");
                        addstationdb.setString(1, stationname);
                        addstationdb.setString(2, stationnode.code);
                        addstationdb.setInt(3, 0);
                        addstationdb.setString(4, prevsname);
                        addstationdb.setString(5, null);
                        addstationdb.setString(6, null);
                        addstationdb.executeUpdate();

                    }
                } else {
                    System.out.println(flag);
                    System.out.println(" no station with that name found ");
                }

            }

            flag = false;

        }

    }

    ArrayList findroute(String sname, String dname) {

        flag = false; // making sure flag is false before traversal
        traverse(mainstation, sname);
        ArrayList<station> al = new ArrayList<>();

        if (!flag) {
            System.out.println(" no source station found fr  ");
            return al;
        } else {

            station foundsname = found;
            flag = false;
            traverse(mainstation, dname);

            if (found.sname.equals(dname)) {
                al = printstation(found, foundsname); // calling to print route
                flag = false;
                return al;
            } else {

                System.out.println(" no destination station found ");
                return al;
            }
        }
    }

    ArrayList printstation(station founddestination, station foundsname) {

        String tempcode = "";
        station temp = foundsname; // a pointer that will traverse
        String desticode = findstationcode(founddestination.sname);
        String sourcode = findstationcode(foundsname.sname);
        ArrayList<station> stationrecord = new ArrayList();

        // case 1 part A ager samjo k maru traversal main statin or root thij che

        if (sourcode.equals("0")) {
            tempcode = "0";
            int i = 1;

            stationrecord.add(temp); // root ne add karyu

            while (!tempcode.equals(desticode)) {
                if (desticode.charAt(i) == '1') {
                    // System.out.print(" --> " + temp.nextl.sname);
                    stationrecord.add(temp.nextl);
                    temp = temp.nextl;
                    tempcode = tempcode + "1";
                    i++;

                } else {
                    // System.out.print(" --> " + temp.nextr.sname);
                    stationrecord.add(temp.nextr);
                    temp = temp.nextr;
                    tempcode = tempcode + "2";
                    i++;

                }

            }

        }

        // case 1 part B ager maru desti 0 che toh

        else if (desticode.equals("0")) {
            temp = foundsname;

            while (!temp.code.equals("0")) {
                // System.out.print(" --> " + temp.sname);
                temp = temp.prev;
                stationrecord.add(temp);
            }
            stationrecord.add(founddestination);

        }

        // case 2 ke ager source ane desti ek left subtree ma ane ek right subtree ma

        else if (desticode.charAt(1) != sourcode.charAt(1)) {
            // toh source thi pehla main station aur head sudhi jasi
            for (int j = 0; j < sourcode.length(); j++) {
                // System.out.print(" --> " + foundsname.sname);
                stationrecord.add(foundsname);
                foundsname = foundsname.prev;
            }

            temp = mainstation; // cuz have main station e che ne
            tempcode = "0";
            int i = 1;

            while (!tempcode.equals(desticode)) {
                if (desticode.charAt(i) == '1') {
                    // System.out.print(" --> " + temp.nextl.sname);
                    temp = temp.nextl;
                    stationrecord.add(temp);
                    tempcode = tempcode + "1";
                    i++;
                } else {

                    temp = temp.nextr;
                    stationrecord.add(temp);
                    tempcode = tempcode + "2";
                    i++;

                }
            }

        }

        // case that is not possible , but can be by users input
        // ager ene source ane desti ekj api didhu toh

        else if (sourcode.equals(desticode)) {
            System.out.println(" YOUR SOURCE STATION AND DESTINATION STATION ARE SAME ");
            System.out.println();
            stationrecord.add(founddestination);

        }

        // case 3 ke ager source and desti nation 2 ekj side of tree ma che
        else if (desticode.charAt(1) == sourcode.charAt(1)) {
            // now ager 2 ek side che toh 2 vache ek station evu hase jeh similar che ,
            // etle apde su kersi , apde for loop lesi jya sudhi 2 na code same etlu apde ek
            // navu code banavsi ,
            // ane e nava code sudhi pochsi apde from source then e nava station code thi
            // desti

            String intersectioncode = "";

            int length;

            // koni length ochi e jova mate // nai joi e toh apde index out of bound jai
            // sake
            length = Math.min(sourcode.length(), desticode.length());

            for (int i = 0; i < length; i++) {
                if (sourcode.charAt(i) == desticode.charAt(i)) {
                    intersectioncode = intersectioncode + sourcode.charAt(i);
                } else {
                    break;
                }
            }

            // now e intersection code uper jasi by having prev or next and check if the
            // source/desti is the intersection

            if (sourcode.equals(intersectioncode) || desticode.equals(intersectioncode)) {
                // have condition k apde prev javanu che k agad javanu che k pachad

                if (sourcode.length() > desticode.length()) {
                    // then means k apde prev javanu che

                    int j = sourcode.length() - 1;
                    // j is used for tracking prev kya sudhi in character

                    tempcode = sourcode;
                    // have tempcode mathi ek ek char delet kerta jasi

                    while (!tempcode.equals(desticode)) {
                        stationrecord.add(temp);
                        temp = temp.prev;
                        tempcode = tempcode.substring(0, (j));
                        j--;
                    }
                    stationrecord.add(founddestination);

                }

                else // means k apde next javanu che // maru source station code ni length ochi che
                {
                    int i = intersectioncode.length(); // kya thi match kervanu chalu kari e

                    while (!intersectioncode.equals(desticode)) {
                        if (desticode.charAt(i) == '1') {
                            // System.out.print(" --> " + temp.sname);
                            stationrecord.add(temp);
                            temp = temp.nextl;
                            i++;
                            intersectioncode = intersectioncode + "1";

                        } else if (desticode.charAt(i) == '2') {
                            // System.out.print(" --> " + temp.sname);
                            stationrecord.add(temp);
                            temp = temp.nextr;
                            i++;
                            intersectioncode = intersectioncode + "2";

                        }
                    }
                    stationrecord.add(founddestination);

                }

            }

            else // maro intersection code source/desti code nathiiii
            {
                // etle pehla source thi intersection station e

                while (!temp.code.equals(intersectioncode)) {
                    // System.out.print(" --> " + temp.sname);
                    stationrecord.add(temp);
                    temp = temp.prev;
                }

                // have apde intersection e ubha chi e
                // etle have intersection this destination

                int i = intersectioncode.length(); // for character ne kya thi check karVIYE

                while (!intersectioncode.equals(desticode)) {
                    if (desticode.charAt(i) == '1') {
                        // System.out.print(" --> " + temp.sname);
                        stationrecord.add(temp);
                        temp = temp.nextl;
                        i++;
                        intersectioncode = intersectioncode + "1";

                    } else if (desticode.charAt(i) == '2') {
                        // System.out.print(" --> " + temp.sname);
                        stationrecord.add(temp);
                        temp = temp.nextr;
                        i++;
                        intersectioncode = intersectioncode + "2";

                    }
                }
                stationrecord.add(founddestination);

            }
        }
        return stationrecord;

    }

    void traverse(station node, String stationname) {
        if (node != null) {
            if (node.sname.equals(stationname)) {
                flag = true;
                found = node;
                return;
            } else if (node.nextl != null) {
                traverse(node.nextl, stationname);
                if (node.nextr != null) {
                    traverse(node.nextr, stationname);
                }
            }
        }
    }

    String findstationcode(String codefindname) {

        flag = false;
        traverse(mainstation, codefindname);

        if (found.sname.equals(codefindname)) {
            return found.code;
        } else {
            return " not found ";
        }
    }

    void findBus() {
        System.out.print("\n ENTER SOURCE STATION :: ");
        String s = hj.nextLine();

        System.out.println("\n ENTER DESTINATION STATION :: ");
        String d = hj.nextLine();

        flag = false; // direct traverse call mate flag false kervo pade
        traverse(mainstation, s);

        if (!flag) {
            System.out.println(" no source found with that name");
        } else {
            flag = false;
            traverse(mainstation, d);

            if (!flag) {
                System.out.println(" no destination found with that name");
            } else {
                routebussuggester(s, d);
                flag = false;
            }

        }
    }

    HashMap<String, HashSet<String>> bs = new HashMap<>(); // for bus at station

    void addBusset(ArrayList<station> al, String busno) {
        for (station sn : al) {

            String code = findstationcode(sn.getsname());
            // getting code of station

            HashSet hs = bs.get(code); // getting prev set and then adding the new bus

            if (hs == null) {
                hs = new HashSet<String>(); // we have to do this cus ager null hase to add method na work kare ne
            }

            hs.add(busno);

            bs.put(code, hs);
            // System.out.println(code + " for " + busno);

        }

        // just to print , its temperory ho bhai
        // System.out.println(bs);
    }

    void removeBusset(ArrayList<station> al, String busno) {
        for (station sn : al) {

            String code = findstationcode(sn.getsname());
            // getting code of station

            HashSet hs = bs.get(code); // getting prev set and then removing the bus

            hs.remove(busno);

            bs.put(code, hs);
        }
    }

    void getBusset(String stname) // for passengers to view which bus will stop at that station
    {
        String code = findstationcode(stname);

        if (bs.get(code).isEmpty()) {
            System.out.println("==> NO BUSES <==");
        } else {

            System.out.println(bs.get(code));
        }
    }

    void routebussuggester(String sn, String dn) {

        String scode = findstationcode(sn);
        String dcode = findstationcode(dn);

        ArrayList<station> al = findroute(sn, dn);

        HashSet hss = new HashSet<>(bs.get(scode));
        // HashSet hsd = new HashSet<>(bs.get(dcode)); // nathi kammnu

        int stcount = 0;

        HashSet result1 = new HashSet<>();

        for (station x : al) {
            String xcode = findstationcode(x.getsname());

            HashSet tempx = new HashSet<>(bs.get(xcode));

            tempx.retainAll(hss);

            if (!tempx.isEmpty()) {
                stcount++;

                result1 = tempx;

                if (x.getsname().equals(dn)) {
                    System.out.println("BUS NO :: " + tempx);
                    // if bus direct mali gai toh
                }
            } else {
                System.out.println("\n" + result1);
                // means direct nathi toh interchange avse aa bus no thi
                break;
            }
        }

        if (stcount != al.size()) // means direct bus nathi malli
        {
            station stinterchange = al.get(stcount - 1); // -1 etle cuz have apde index joi e che
            System.out.println("INTERCHANGE AT " + stinterchange.getsname());

            String stintercode = findstationcode(stinterchange.getsname());

            HashSet tempst = new HashSet<>(bs.get(stintercode));

            // have interchange ne desti thi compare

            HashSet tempy = new HashSet<>(bs.get(dcode));

            tempy.retainAll(tempst);

            if (!tempy.isEmpty()) {
                System.out.println(tempy);
            } else {
                System.out.println(" NO BUS AVAILABLE ");
            }
        }

    }

    int calculateFair(String ss, String ds) {

        double totaldistance = 0;
        int fair = 0;

        ArrayList<station> al = findroute(ss, ds);

        for (station obj : al) {
            totaldistance += obj.prevdistance;
        }

        if (totaldistance == 1) {
            fair = 0;
        } else if (totaldistance <= 2) {
            fair = 5;
        } else if (totaldistance <= 4) {
            fair = 10;
        } else if (totaldistance <= 6) {
            fair = 15;
        } else {
            fair = 25;
        }

        return fair;
    }

    void printlist(ArrayList<station> al) {
        System.out.println("\n===========================================");
        for (station node : al) {
            System.out.println(" --> " + node.getsname());
        }
        System.out.println("\n===========================================");
        System.out.println("");
    }

    static Connection con;

    public static void main(String[] args) throws Exception {

        // JDBC CONNECTION
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BRTS", "root", "");
        if (con != null) {
            System.out.println("JDBC CONNECTED SUCCEFULLY ");
        }

        Scanner hj = new Scanner(System.in);

        // testcase -- A PART OF MAP OF AHMEDABAD BRTS

        // creating stations

        brts objmain = new brts();
        objmain.addstation("nehrunager", null, 0);
        objmain.addstation("jhansi ki rani", "nehrunager", 0.200);
        objmain.addstation("shivranjni", "jhansi ki rani", 0.120);
        objmain.addstation("himmatlal park", "shivranjni", 0.190);
        objmain.addstation("andhjan mandal", "himmatlal park", 0.439);
        objmain.addstation("university", "andhjan mandal", 0.260);
        objmain.addstation("memnager", "university", 0.340);
        objmain.addstation("valinath chowk", "memnager", 0.300);
        objmain.addstation("sola crossroad", "valinath chowk", 0.200);
        objmain.addstation("parasnager", "sola crossroad", 0.344);
        objmain.addstation("jodhpur char rasta", "shivranjni", 0.460);
        objmain.addstation("star bazar", "jodhpur char rasta", 0.330);
        objmain.addstation("isro", "star bazar", 0.400);
        objmain.addstation("ramdevnager", "isro", 0.430);
        objmain.addstation("iskon", "ramdevnager", 0.380);
        objmain.addstation("karnavati club", "iskon", 0.450);
        objmain.addstation("makerba road", "karnavati club", 0.600);
        objmain.addstation("prahalnager crossroad", "makerba road", 0.400);
        objmain.addstation("sanad circle", "prahalnager crossroad", 1.930);
        objmain.addstation("science city", "parasnager", 0.300);
        objmain.addstation("jaimangle", "sola crossroad", 0.250);
        objmain.addstation("shastrinager", "jaimangle", 0.300);
        objmain.addstation("pragatinager", "shastrinager", 0.200);
        objmain.addstation("akhbarnager", "pragatinager", 0.150);
        objmain.addstation("sabarmati power house", "akhbarnager", 0.200);
        objmain.addstation("rathi apartment", "sabarmati power house", 0.300);
        objmain.addstation("south bopal", "iskon", 0.400);

        objmain.addBusset(objmain.findroute("nehrunager", "jaimangle"), "101");
        objmain.addBusset(objmain.findroute("shivranjni", "himmatlal park"), "12u");
        objmain.addBusset(objmain.findroute("science city", "pragatinager"), "16d");
        objmain.addBusset(objmain.findroute("jaimangle", "shivranjni"), "17e");
        objmain.addBusset(objmain.findroute("iskon", "nehrunager"), "1u");
        objmain.addBusset(objmain.findroute("jaimangle", "star bazar"), "3u");
        objmain.addBusset(objmain.findroute("sola crossroad", "jhansi ki rani"), "9u");
        objmain.addBusset(objmain.findroute("pragatinager", "south bopal"), "4D");

        // user interface

        admin objAdmin = new admin(objmain, con);
        stationworker objsw = new stationworker(con, objmain);
        Driver objdriv = new Driver(con, objmain);
        report objreport = new report(con);

        while (true) {
            System.out.println("================= BRTS PORTAL ================");
            System.out.println("| 1. ADMIN PORTAL                            |");
            System.out.println("| 2. STATION WORKER PORTAL                   |");
            System.out.println("| 3. PASSENGER PORTAL                        |");
            System.out.println("| 4. DRIVER PORTAL                           |");
            System.out.println("| 5. BRTS CENTRAL PORTAL                     |");
            System.out.println("==============================================");
            int choice = hj.nextInt();

            switch (choice) {

                case 1: {

                    boolean c = false;
                    System.out.println(" ENTER ADMIN ID :: ");
                    hj.nextLine();
                    String id = hj.nextLine();
                    System.out.println(" ENTER ADMIN PASSWORD :: ");
                    String pass = hj.nextLine();

                    String query = " select * from admin_details where a_id = '" + id + "' and a_pass = '" + pass
                            + "';";

                    ResultSet rs = con.createStatement()
                            .executeQuery(query);

                    if (!rs.next()) {
                        System.out.println(" ==> REGESTER ADMIN AT BRTS CENTER ");
                    } else {
                        c = true;
                    }

                    while (c) {

                        System.out.println("=============== ADMIN PORTAL ===============");
                        System.out.println("| 1. ADD BUS TO ROUTE                      |");
                        System.out.println("| 2. REMOVE BUS FROM ROUTE                 |");
                        System.out.println("| 3. DECLARE BUS BREAKDOWN                 |");
                        System.out.println("| 4. REVIEW DAILY REPORT                   |");
                        System.out.println("| 5. ADD STATION WORKER                    |");
                        System.out.println("| 6. LOG OUT                               |");
                        System.out.println("============================================");
                        int choicee = hj.nextInt();

                        switch (choicee) {
                            case 1: {
                                objAdmin.addBus();
                                break;
                            }
                            case 2: {
                                System.out.print(" \n ENTER BUS NUMBER TO BE REMOVED :: ");
                                String busno = hj.next();
                                objAdmin.removeBUS(busno);
                                break;
                            }
                            case 3: {
                                System.out.print(" \n ENTER BUS NUMBER FOR STATUS CHANGE  :: ");
                                String busno = hj.next();
                                objAdmin.breakdownbus(busno);
                                break;
                            }
                            case 4: {
                                objreport.generate();
                                break;
                            }
                            case 5: {
                                objsw.workercreate(objmain);
                                break;
                            }
                            case 6: {
                                c = false;
                                break;
                            }
                            default: {
                                System.out.println(" \n INVALID CHOICE");
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("========= STATION WORKER PORTAL ========");
                    objsw.log();
                    break;
                }
                case 3: {

                    boolean exit = false;
                    while (!exit) {
                        System.out.println("========= PASSENGER PORTAL ========");
                        System.out.println("| 1.ROUTE INFO                     |");
                        System.out.println("| 2.BUS INFO                       |");
                        System.out.println("| 3.FAIR CALCULATION               |");
                        System.out.println("| 4.BUS SCHEDULE                   |");
                        System.out.println("| 5.FIND BUS NUMBER                |");
                        System.out.println("| 6.STATION-BUS INFO               |");
                        System.out.println("| 7.EXIT                           |");
                        System.out.println("====================================");
                        int choice3 = hj.nextInt();

                        switch (choice3) {

                            case 1: {
                                System.out.print(" ENTER SOURCE STATION :: ");
                                hj.nextLine();
                                String ss = hj.nextLine();
                                System.out.print(" ENTER DESTINATION STATION :: ");
                                String ds = hj.nextLine();
                                ArrayList<brts.station> fr = objmain.findroute(ss, ds);
                                objmain.printlist(fr);
                                break;
                            }
                            case 4: {
                                System.out.print(" ENTER BUS NUMBER :: ");
                                hj.nextLine();
                                String bn = hj.nextLine();

                                // System.out.println(" joyu for " + bn);

                                // use of inner join
                                String query = " select * from bus_schedule inner join bus_details on bus_details.bus_number = bus_schedule.busno where bus_schedule.busno = ? and eetime>current_time and status not like 'BREAKDOWN'";
                                PreparedStatement pst1 = con.prepareStatement(query);
                                pst1.setString(1, bn);
                                ResultSet rs1 = pst1.executeQuery();

                                if (!rs1.next()) {
                                    System.out.println(" NO SCHEDULE FOUND FOR BUS NUMBER " + bn);
                                } else {
                                    System.out.println("========= BUS INFO ===========");
                                    System.out.println(" BUS NO      |   START TIME  |");
                                    do {
                                        System.out.println(bn + "         " + "|" + rs1.getTime(2));
                                        if (rs1.getTime(5) != null) {
                                            System.out.println(" DELAYED BY " + rs1.getTime(5));
                                        }
                                    } while (rs1.next());
                                }
                                break;
                            }
                            case 2: {
                                System.out.print(" ENTER BUS NUMBER :: ");
                                hj.nextLine();
                                String bn = hj.nextLine();
                                String query = " select * from bus_details where bus_number = ? ";
                                PreparedStatement pst = con.prepareStatement(query);
                                pst.setString(1, bn);
                                ResultSet rs = pst.executeQuery();

                                if (!rs.next()) {
                                    System.out.println("NO BUS FOUND ");
                                } else {

                                    System.out.println(" BUS NO :: " + rs.getString(1));
                                    System.out.println(" source station :: " + rs.getString(2));
                                    System.out.println(" destination station :: " + rs.getString(3));
                                    System.out.println(" one way distance :: " + rs.getInt(4));
                                    System.out.println(" bus status :: " + rs.getString(5));
                                }
                                break;
                            }
                            case 3: {
                                System.out.println(" ENTER SOURCE STATION :: ");
                                hj.nextLine();
                                String ss = hj.nextLine();

                                objmain.traverse(objmain.mainstation, ss);
                                if (objmain.flag) {
                                    objmain.flag = false;

                                    System.out.println(" ENTER DESTINATION STATION :: ");
                                    String ds = hj.nextLine();
                                    objmain.traverse(objmain.mainstation, ds);

                                    if (objmain.flag) {

                                        System.out.println(" FAIR " + objmain.calculateFair(ss, ds));
                                        objmain.flag = false;
                                    } else {
                                        System.out.println("==> NO DESTINATION FOUND <==");
                                    }
                                } else {
                                    System.out.println("==> NO STATION  FOUND <==");
                                }
                                break;
                            }
                            case 5: {
                                objmain.findBus();
                                break;
                            }
                            case 6: {
                                System.out.print(" ENTER STATION NAME :: ");
                                hj.nextLine();
                                String sn = hj.nextLine();
                                objmain.getBusset(sn);
                                break;
                            }
                            case 7: {
                                System.out.println("============ THANK YOU FRO USING PASSENGER PORTAL =============");
                                exit = true;
                                break;
                            }
                            default: {
                                System.out.println(" \n INVALID CHOICE");
                            }
                        }
                    }
                    break;

                }
                case 4: {

                    boolean fb = false;

                    while (!fb) {

                        System.out.println("============ DRIVER PORTAL ==============");
                        System.out.println("| 1.log in                               |");
                        System.out.println("| 2.exit                                 |");
                        System.out.println("==========================================");
                        int choicee = hj.nextInt();

                        switch (choicee) {
                            case 1: {
                                System.out.println(" ENTER DRIVER ID :: ");
                                hj.nextLine();
                                String id = hj.nextLine();
                                objdriv.login(id);
                                break;
                            }
                            case 2: {
                                fb = true;
                                break;
                            }
                            default: {
                                System.out.println(" \n INVALID CHOICE");
                            }
                        }
                    }
                    break;

                }
                case 5: {

                    System.out.println("============ CENTRAL BRTS PORTAL ===========");
                    System.out.println("| 1.log in                                 |");
                    System.out.println("============================================");
                    int choiceee = hj.nextInt();

                    if (choiceee == 1) {

                        String id = "1221";

                        // time based log in

                        System.out.print(" ENTER CBRTSPID :: ");
                        hj.nextLine();
                        String idd = hj.nextLine();

                        System.out.print(" ENTER PASSCODE :: ");
                        String pass = hj.nextLine();

                        String query = "select current_time ";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        rs.next();

                        String p = rs.getTime(1) + "";
                        String[] parr = p.split(":");
                        p = parr[0] + parr[1];

                        if (pass.equals(p) && id.equals(idd)) {

                            boolean exit = false;

                            while (!exit) {

                                System.out.println("===================== WELCOME TO CBRTS PORTAL ===============");
                                System.out.println("| 1.  view all buses                                        |");
                                System.out.println("| 2.  ADD STATION                                           |");
                                System.out.println("| 3.  DAILY REPORT                                          |");
                                System.out.println("| 4.  EXIT                                                  |");
                                System.out.println("==============================================================");
                                int ch = hj.nextInt();

                                switch (ch) {
                                    case 1: {
                                        System.out.println("============ BUS NUMBERS ==============");
                                        for (String x : objAdmin.businfo) {
                                            System.out.print(x + " , ");
                                        }
                                        System.out.println("\n========================================");
                                        break;
                                    }
                                    case 2: {
                                        System.out.print(" ENTER STATION NAME :: ");
                                        hj.nextLine();
                                        String stationname = hj.nextLine();
                                        System.out.print(" ENTER PREVIOUS STATION NAME :: ");
                                        String prevsname = hj.nextLine();
                                        System.out.print(" ENTER STATION DISTANCE FROM PREVIOUS :: ");
                                        double dist = hj.nextDouble();
                                        objmain.addstation(stationname, prevsname, dist);
                                        break;
                                    }
                                    case 3: {
                                        objreport.generate();
                                        break;
                                    }
                                    case 4: {
                                        System.out.println(" THANK YOU FOR USING CBRTS PORTAL ");
                                        exit = true;
                                    }
                                }
                            }

                        } else {
                            System.out.println(" INVALID PASSWORD OR CBRTSPID ");
                        }

                    } else {
                        System.out.println(" INVALID INPUT ");
                    }
                    break;
                }

                default: {
                    System.out.println(" INVALID INPUT ");
                    break;
                }
            }
        }

    }
}
