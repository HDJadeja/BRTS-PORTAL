import java.util.*;
import java.io.*;
import java.sql.*;

class passUser // use of data strucutre , which were available inbuilt
{
    String name;
    int regi_no;
    String password;
    String uniquepasscode;
    int balance = 0;

    passUser(String name, String password, int rn) {
        this.name = name;
        this.regi_no = rn;
        this.password = password;
        this.uniquepasscode = name.charAt(1) + (int) Math.random() + "" + password.charAt(1);
        System.out.println(" ==> PASS ID :: " + uniquepasscode);
    }
}

class passSLL {

    Connection conn;

    passSLL(Connection con) {
        conn = con;
    }

    class node {
        passUser obj;
        node next;

        node(passUser obj) {
            this.obj = obj;
        }
    }

    node head;

    void INSERTpassUser(String name, String password, int rn) {

        node n = new node(new passUser(name, password, rn));

        if (head == null) {
            head = n;
        } else {
            node l = head;

            while (l.next != null) {
                l = l.next;
            }
            l.next = n;
        }
    }

    void rechargePass(String passno, int amt) {
        node l = head;
        boolean done = false;

        while (l != null) {
            if (l.obj.uniquepasscode.equals(passno)) {
                l.obj.balance += amt;
                System.out.println(" RECHARGED PASS NUMBER " + l.obj.uniquepasscode);
                done = true;
                break;
            }
            l = l.next;
        }
        if (!done) {
            System.out.println("==> NO PASS FOUND <==");
        }
    }

    void deductFair(String passno, int fair) throws SQLException {
        node l = head;
        boolean done = false;

        while (l != null) {
            if (l.obj.uniquepasscode.equals(passno)) {
                l.obj.balance -= fair;
                done = true;
                break;
            }
            l = l.next;
        }
        if (!done) {
            System.out.println("==> NO PASS FOUND <==");
        }
    }

    void deductFine(String passno) {
        node l = head;
        boolean done = false;

        while (l != null) {
            if (l.obj.uniquepasscode.equals(passno)) {
                l.obj.balance -= 100;
                System.out.println(" FINE OF RS 100 DEDUCTED FROM " + l.obj.name + "'s ACCOUNT");
                done = true;
                break;
            }
            l = l.next;
        }
        if (!done) {
            System.out.println("==> NO PASS FOUND <==");
        }
    }

    void balanceShower(String passno) {
        node l = head;
        boolean done = false;
        while (l != null) {
            if (l.obj.uniquepasscode.equals(passno)) {
                System.out.println(" ==> BALANCE :: " + l.obj.balance + " \n");
                done = true;
            }
            l = l.next;
        }
        if (!done) {
            System.out.println("==> NO PASS FOUND <==");
        }
    }

    void cancelPass(String passno) {
        boolean found = false;
        node l = head;

        // first checking if it is

        while (l != null) {
            if (l.obj.uniquepasscode.equals(passno)) {
                found = true;
            } else {
                l = l.next;
            }
        }

        if (found) {
            if (head.obj.uniquepasscode.equals(passno)) {
                head = head.next;
            }

            else {

                node l2 = head;

                while (!l2.next.obj.uniquepasscode.equals(passno)) {
                    l2 = l2.next;
                }
                l2.next = l2.next.next;

            }
        } else {
            System.out.println(" ==> PASS NOT FOUND <==");
        }
    }
}