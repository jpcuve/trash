package lang.interpreter;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

public class Interactive extends JFrame implements ActionListener, CaretListener {
    private Environment env;
    private Interpreter ip;
    private Parser p;
    private Core c;
    private JTextField inp;
    private JTextArea out;
    private DefaultHighlighter high;
    private DefaultHighlighter.DefaultHighlightPainter hp;

    private AttributeSet normal;
    private AttributeSet highlight;

    public Interactive() {
        ip = new Interpreter();
		env = new Environment();
        p = new Parser();
        Core c = new Core();
        try {
            c.bind(p, env);
        } catch (InterpreterException e) {
        }
        this.setTitle("Interpreter");
        JPanel mp = new JPanel();
        mp.setLayout(new BorderLayout());
        inp = new JTextField(32);
        high = new DefaultHighlighter();
        hp = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
        inp.setHighlighter(high);
        inp.addActionListener(this);
        inp.addCaretListener(this);
        mp.add(inp, BorderLayout.SOUTH);
        out = new JTextArea(16, 32);
        mp.add(out, BorderLayout.CENTER);
        this.getContentPane().add(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Interactive mf = new Interactive();
        mf.pack();
        mf.setVisible(true);
    }

    public void caretUpdate(CaretEvent event) {
        int dot = event.getDot();
        String s = inp.getText();
        high.removeAllHighlights();
        if (dot > 1) {
            char c = s.charAt(dot - 1);
            if (c == ')') {
                int count = 1;
                int i = dot - 2;
                while(i >= 0 && count > 0) {
                    switch(s.charAt(i)) {
                        case ')':
                            count++;
                            break;
                        case '(':
                            count--;
                            break;
                    }
                    i--;
                }
                if (count == 0) {
                    try {
                        high.addHighlight(dot - 1, dot, hp);
                        high.addHighlight(i + 1, i + 2, hp);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent event) {
        StringReader sr = new StringReader(inp.getText());
        try {
            Expression e = p.parse(new Tokenizer(sr));
            System.out.println("Babel to parse=" + e.write());
            if (e != null) {
                Expression v = ip.eval(e, env);
                out.append(e.write() + " -> ");
                if (v != null) {
                    out.append(v.write());
                }
                out.append("\n");
                inp.setText("");
            }
        } catch(Exception ex){
            out.append(ex.getMessage() + "\n");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Syntax error", JOptionPane.ERROR_MESSAGE);
        }
    }

}