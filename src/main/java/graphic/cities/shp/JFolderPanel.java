package graphic.cities.shp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2010
 * Time: 3:14:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JFolderPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(JFolderPanel.class);
    private int index = 0;
    private File[] files = null;
    private Action aNext = new AbstractAction("next"){
        public void actionPerformed(ActionEvent actionEvent) {
            LOG.debug("next");
            if (files != null){
                index++;
                if (index >= files.length) index = 0;
                select();
            }
        }
    };
    private Action aPrev = new AbstractAction("prev"){
        public void actionPerformed(ActionEvent actionEvent) {
            LOG.debug("prev");
            if (files != null){
                index--;
                if (index < 0) index = files.length - 1;
                select();
            }
        }
    };
    private JComboBox cbFilenames = new JComboBox();
    private JLabel lFilename = new JLabel();
    private JPanel pCenter = new JPanel();

    public JFolderPanel(String folder) {
        final File fFolder = new File(folder);
        if (fFolder.isDirectory()){
            files = fFolder.listFiles(new FileFilter(){
                public boolean accept(File file) {
                    return file.isFile() && file.getName().endsWith(".shp");
                }
            });
            for (final File f: files){
                ((DefaultComboBoxModel) cbFilenames.getModel()).addElement(f);
            }
        }
        setLayout(new BorderLayout());
        final JToolBar toolBar = new JToolBar();
        toolBar.add(aNext);
        toolBar.add(aPrev);
        cbFilenames.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                final File sel = (File) cbFilenames.getSelectedItem();
                int i = 0;
                while(i < files.length && files[i] != sel) i++;
                if (i < files.length){
                    index = i;
                    select();
                }

            }
        });
        toolBar.add(cbFilenames);
        toolBar.add(lFilename);
        add(BorderLayout.NORTH, toolBar);
        pCenter.setLayout(new BorderLayout());
        aNext.actionPerformed(null);
        add(BorderLayout.CENTER, pCenter);
    }

    private void select(){
        lFilename.setText(files[index].getName());
        pCenter.removeAll();
        pCenter.add(BorderLayout.CENTER, new JShapePanel(files[index].getAbsolutePath()));
        pCenter.repaint();
    }

    public static void main(String[] args) {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JFolderPanel("eur"));
        f.pack();
        f.setVisible(true);
    }
}
