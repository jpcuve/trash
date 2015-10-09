/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 15-May-02
 * Time: 14:12:35
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

public class Calculator extends JFrame
implements ActionListener, MachineListener {
    private Machine m;
    private Environment env;
    private JTextField inp;
    private JTextArea out;

    public Calculator() {
        m = new Machine();
        env = new Environment();
        m.addMachineListener(this);
        this.setTitle("Calculator");
        JPanel mp = new JPanel();
        mp.setLayout(new BorderLayout());
        inp = new JTextField(32);
        inp.addActionListener(this);
        mp.add(inp, BorderLayout.SOUTH);
        out = new JTextArea(16, 32);
        mp.add(out, BorderLayout.CENTER);
        this.getContentPane().add(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Calculator mf = new Calculator();
        mf.pack();
        mf.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        StringReader sr = new StringReader(inp.getText());
        Parser p = new Parser(new Tokenizer(sr));
        m.clear();
        try {
            p.parse(m);
            m.run(env);
        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Syntax error", JOptionPane.ERROR_MESSAGE);
        }
        inp.setText("");
    }

    public void machineDisplay(MachineEvent ev) {
        out.append(ev.getMessage() + "\n");
    }
}
