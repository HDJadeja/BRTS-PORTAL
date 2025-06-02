// understanding java gui 

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout; // for layout 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class jg {
    public static void main(String[] args) {

        abc obj = new abc();
        obj.add();

    }
}

class abc extends JFrame implements ActionListener {

    JLabel l;

    abc() {
        // // by default jframe is not visible we have to make it visible
        // setVisible(true);

        // // to make size of jframe
        // setSize(800, 600);

        // to show something on jframe we need jlabel
        // jlabel is a class of swing
        // ena contructor ma apde jeh show kervu hoi e pass kariye

        // JLabel l = new JLabel("BRTS PORTAL");

        // // to add text to jframe
        // add(l);

        // // adding another label
        // JLabel l1 = new JLabel("BRTS PORTAL2");
        // add(l1);

        // ager atlu kersi to l2 j dekhase l nu nai dekhai
        // as it follows cardlayout

        // to solve that we change layout
        // many layout option are grid , flow ,null
        // in flow bydefault all will take place at middle

        // setLayout(new FlowLayout());

        // aa na kerva thi apde jyare jframe ne close kerta tyrae code toh chaluj reht
        // to ene close kerva thi apnu code pan close thai ena mate apde aa use ka
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // exit on clode is a consant
        // we can also use numbers

    }

    void add() {

        // pehla badhu add pachi set visible ne e badhu

        // to get data

        JTextField t = new JTextField(10); // ena consrtuctor ma apde textbox ni size pass kari sakai
        // user apelo textlev mate
        // t.getText();

        // for button
        JButton b = new JButton("click me"); // pass the message to show on button
        // e button ne click kari etle kai action thavi joi e
        b.addActionListener(this); // this needs obj of actionlistner interface in its constructor // ane interface
                                   // na obj banne nai
        // toh ena mate apde implemnts actionl karavi desi
        // but as interface ni badhi method thavi joie override toh

        add(t);
        add(b);
        setVisible(true);
        setSize(800, 400);
        l = new JLabel();
        add(l);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(2);
    }

    public void actionPerformed(ActionEvent ae) {

        l.setText("ha ho");

    }
}
