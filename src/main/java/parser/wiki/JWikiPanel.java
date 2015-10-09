package parser.wiki;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.Method;

public class JWikiPanel extends JPanel {
    private static final Dimension PREFERRED = new Dimension(1000, 1000);
    private final JTextArea taInput = new JTextArea();
    private final JTextArea taOutput = new JTextArea();
    private final JPanel pTree = new JPanel(new BorderLayout());
    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance("wiki.WikiDocumentBuilderFactory", getClass().getClassLoader());

    public JWikiPanel() {
        super(new BorderLayout());
        final JTabbedPane tpMain = new JTabbedPane();
        tpMain.addTab("Input", new JScrollPane(taInput));
        tpMain.addTab("Output", new JScrollPane(taOutput));
        tpMain.addTab("Tree", pTree);
        tpMain.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tpMain.getSelectedIndex() > 0) convert();
            }
        });
        add(BorderLayout.CENTER, tpMain);
        final JToolBar tbMain = new JToolBar();
        tbMain.add(new AbstractAction("Browser"){
            public void actionPerformed(ActionEvent e) {
                convert();
                openBrowser(new ByteArrayInputStream(taOutput.getText().getBytes()), "html");
            }
        });
        add(BorderLayout.PAGE_START, tbMain);
    }

    private void convert(){
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(new InputSource(new StringReader(taInput.getText())));
            pTree.removeAll();
            pTree.add(BorderLayout.CENTER, new JScrollPane(new JNodeTree(document)));
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            final StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            taOutput.setText(stringWriter.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public Dimension getPreferredSize() {
        return PREFERRED;
    }

    public static void openBrowser(final InputStream is, final String extension) {
        final byte[] buffer = new byte[1024 * 1024];
        FileOutputStream os = null;
        try{
            final File file = File.createTempFile("page", "." + extension);
            os = new FileOutputStream(file);
            int read;
            while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
            openBrowser("file:" + file.getAbsolutePath());
        } catch(IOException x){
            x.printStackTrace();
        } finally{
            if (os != null) try{
                os.close();
            } catch(IOException x){
                x.printStackTrace();
            }
            if (is != null) try{
                is.close();
            } catch(IOException x){
                x.printStackTrace();
            }
        }
    }

    public static void openBrowser(final String url){
        final String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                final Class fileMgr = Class.forName("com.apple.eio.FileManager");
                final Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
                openURL.invoke(null, url);
            } else if (osName.startsWith("Windows")){
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                //assume Unix or Linux
                final String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0) browser = browsers[count];
                if (browser == null) throw new Exception("Could not find web browser");
                else Runtime.getRuntime().exec(new String[] {browser, url});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final JFrame fMain = new JFrame();
        fMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fMain.add(new JWikiPanel());
        fMain.pack();
        fMain.setVisible(true);
    }
}
