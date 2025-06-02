import java.util.*;
import java.io.*;
import java.sql.*;

class stationworker extends brts {
    // brts extends cuzz kem k mare brts no inner class ni jarur padvani che

    Scanner hj = new Scanner(System.in);

    String stationname;
    String wname;
    String wcontact;
    int workerid = 1769735;
    int password;
    boolean log = false;
    Connection con;
    brts objbrts;
    passSLL objPassSLL;

    stationworker(Connection con, brts objbrts) {
        this.con = con;
        this.objbrts = objbrts;
        objPassSLL = new passSLL(con);
    }

    Hashtable<Integer, Integer> workerdetails = new Hashtable();

    void workercreate(brts objmain) throws SQLException {
        System.out.print(" ENTER WORKER NAME :: ");
        wname = hj.nextLine();
        System.out.print(" ENTER WORKER CONTACT NUMBER :: ");
        wcontact = hj.nextLine();
        System.out.println(" WORKER ID GENERATED IS :: " + workerid);
        System.out.print(" ENTER PASSWORD (NUMBERS ONLY) :: ");
        password = hj.nextInt();
        System.out.print("\n ENTER STATION NAME :: ");
        hj.nextLine();
        stationname = hj.nextLine();
        objmain.traverse(objmain.mainstation, stationname);

        if (objmain.flag) {

            workerdetails.putIfAbsent(workerid, password);

            CallableStatement addemp = con.prepareCall("{call ADDemployee(?,?,?)}");
            addemp.setInt(1, workerid);
            addemp.setString(2, wname);
            addemp.setString(3, stationname);
            addemp.execute();

            System.out.println(" ================================================  ");
            System.out.println(" | WORKER ID    :: " + workerid++);
            System.out.println(" | PASSWORD     :: " + password);
            System.out.println(" | STATION NAME :: " + stationname);
            System.out.println(" ================================================  ");
            // System.out.println(" obj main nu flag che " + objmain.flag);
            // System.out.println(" obj brts nu flag che " + objbrts.flag);
            objmain.flag = false;
        } else {
            System.out.println("==> STATION NOT FOUND <==");
        }
    }

    void resetpass() {
        System.out.println(" ENTER YOUR WORKER ID :: ");
        int tempid = hj.nextInt();

        if (workerdetails.containsKey(tempid)) {
            System.out.println(" ENTER YOUR NEW PASSWORD :: ");
            int newpass = hj.nextInt();

            workerdetails.put(tempid, newpass);

            System.out.println(" ++++++++++++ PASSWORD UPDATED ++++++++++++ \n");
        }

        else {
            System.out.println(" NO WORKER ID FOUND \n");
        }
    }

    int tempid; // to check trace of loggin id

    int rn = 1001;

    void log() throws Exception {

        System.out.println(" | PRESS 1 :: LOG IN              | ");
        System.out.println(" | PRESS 2 :: RESET PASS          | ");
        System.out.println(" ================================== ");
        int choice = hj.nextInt();

        switch (choice) {
            case 1: {
                if (log) {
                    System.out.println(" YOU ARE ALREADY LOGGED IN \n");
                } else {
                    System.out.println(" ENTER YOUR WORKER ID :: ");
                    tempid = hj.nextInt();
                    System.out.println(" ENTER YOUR PASSWORD :: ");
                    int temppass = hj.nextInt();
                    if (workerdetails.containsKey(tempid) && workerdetails.get(tempid) == temppass) {
                        System.out.println(" ========= WELCOME TO SW PORTAL =========== ");
                        log = true;
                        CallableStatement login = con.prepareCall("{call employeeLOGin(?)}");
                        login.setInt(1, tempid);
                        login.execute();

                        boolean logoutbool = false;

                        while (!logoutbool) {

                            System.out.println(" ================= " + stationname + " ==============");
                            System.out.println(" | 1.PRINT TICKET                         |");
                            System.out.println(" | 2.ADD STATION WORKER                   |");
                            System.out.println(" | 3.REGISTER PASS                        |");
                            System.out.println(" | 4.PASS RECHARGE                        |");
                            System.out.println(" | 5.CANCEL PASS                          |");
                            System.out.println(" | 6.PASS BALANCE                         |");
                            System.out.println(" | 7.PRINT FINE                           |");
                            System.out.println(" | 8.LOG OUT                              |");
                            System.out.println(" =========================================");
                            int choice2 = hj.nextInt();

                            switch (choice2) {
                                case 1: {
                                    System.out.print(" ENTER DESTINATION NAME :: ");
                                    hj.nextLine();
                                    String dest = hj.nextLine();
                                    ticket(stationname, dest);
                                    break;
                                }
                                case 2: {
                                    workercreate(objbrts);
                                    break;
                                }
                                case 3: {
                                    System.out.print("\n ENTER PASSHOLDER NAME :: ");
                                    hj.nextLine();
                                    String phname = hj.nextLine();
                                    System.out.print(" ENTER PASSHOLDER password :: ");
                                    String phpassword = hj.nextLine();
                                    objPassSLL.INSERTpassUser(phname, phpassword, rn++);
                                    break;
                                }
                                case 4: {
                                    System.out.print(" ENTER PASS NUMBER :: ");
                                    hj.nextLine();
                                    String passnum = hj.nextLine();
                                    System.out.print(" ENTER AMOUNT :: ");
                                    int amount = hj.nextInt();
                                    objPassSLL.rechargePass(passnum, amount);
                                    break;
                                }
                                case 5: {
                                    System.out.print(" ENTER PASS NUMBER :: ");
                                    hj.nextLine();
                                    String passnum = hj.nextLine();
                                    objPassSLL.cancelPass(passnum);
                                    break;
                                }
                                case 6: {
                                    System.out.print(" ENTER PASS NUMBER :: ");
                                    hj.nextLine();
                                    String pn = hj.nextLine();
                                    objPassSLL.balanceShower(pn);
                                    break;
                                }
                                case 7: {
                                    fine();
                                    break;
                                }
                                case 8: {
                                    if (!log) {
                                        System.out.println(" NO LOGGIN FOUND ");
                                    } else {

                                        CallableStatement logout = con.prepareCall("{call employeeLOGout(?)}");
                                        logout.setInt(1, tempid);
                                        logout.execute();

                                        System.out.println(" SUCCESFULLY LOGGED OUT ");
                                        log = false;
                                        logoutbool = true;
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println(" NO ID OR PASSWORD FOUND \n");
                    }
                }
                break;
            }
            case 2: {
                resetpass();
                break;
            }
            default: {
                System.out.println(" INVALID INPUT ");
            }
        }
    }

    void ticket(String SourceStation, String destistation) throws Exception {

        File f = new File("ticket.text");
        f.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

        objbrts.traverse(objbrts.mainstation, destistation);

        if (objbrts.flag) {

            int fair = objbrts.calculateFair(SourceStation, destistation);

            String ticno = wname + Integer.toString(workerid).substring(0, 3) + " ";

            bw.write("======== TRAVEL TICKET ============");
            bw.newLine();
            bw.write("|| TICKET NUMBER :: " + ticno + " \n" + "|| TYPE TRANSIT ::  BRTS \n|| PAYMENT TYPE :: CASH ");
            bw.newLine();
            bw.write("|| SOURCE STATION :: " + SourceStation + " \n|| DESTINATION " + destistation);
            bw.newLine();
            bw.write("========== TOTAL FAIR Rs." + fair + "========");
            bw.close();

            CallableStatement cst1 = con.prepareCall("{ call addrevenue(?,?)}");
            cst1.setString(1, stationname);
            cst1.setInt(2, fair);
            cst1.execute();

        } else {
            System.out.println(" NO STATION FOUND ");
        }
    }

    void fine() throws Exception {
        System.out.println(" ENTER 1 TO COLLECT FINE AMOUNT BY CASH ");
        System.out.println(" ENTER 2 TO COLLECT FINE AMOUNT FROM PASS ");
        int inp = hj.nextInt();

        if (inp == 1) {

            File f = new File("fine_ticket.text");
            f.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            String fticno = wname + Integer.toString(workerid).substring(0, 3) + " ";

            bw.write("======== BRTS FINE TICKET =============== ");
            bw.newLine();
            bw.write("| FINE NUMBER :: " + fticno);
            bw.newLine();
            bw.write("| FINE AMOUNT :: 100 RS                 |");
            bw.newLine();
            bw.write("| PAYMENT TYPE :: CASH                  |");
            bw.newLine();
            bw.write("=========================================");
            bw.close();

            CallableStatement cst1 = con.prepareCall("{ call addrevenue(?,?)}");
            cst1.setString(1, stationname);
            cst1.setInt(2, 100);
            cst1.execute();

            System.out.println(" FINE TICKET PRINTED ");
        } else if (inp == 2) {
            System.out.println(" \n ENTER PASS NUMBER :: ");
            hj.nextLine();
            String passno = hj.nextLine();
            objPassSLL.deductFine(passno);
        }
    }
}
