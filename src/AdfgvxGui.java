import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class AdfgvxGui extends JFrame {

    private static final long serialVersionUID = 1L;

    private Adfgvx adfgvx;

    private JTextField keyText;
    private JTextField clearTextIn;
    private JLabel encodedTextOut;
    private JTextField codedTextIn;
    private JLabel decodedTextOut;

    AdfgvxGui() {
        super("Adfgvx encoding/decoding by Mini890");
        setLayout(new BorderLayout());

        JPanel textPanel = new JPanel(new GridLayout(15, 1));

        textPanel.add(new JLabel());
        textPanel.add(createCenteredLabel("Enter the Key here:"));
        textPanel.add(createCenteredLabel("(without a key no transaltion would happen)"));
        keyText = new JTextField(50);
        keyText.getDocument().addDocumentListener(new KeyListener());
        textPanel.add(keyText);
        textPanel.add(new JLabel());

        textPanel.add(createCenteredLabel("Enter text to encode here"));
        clearTextIn = new JTextField(50);
        clearTextIn.getDocument().addDocumentListener(new ClearListener());
        textPanel.add(clearTextIn);
        textPanel.add(createCenteredLabel("Coded Text"));
        encodedTextOut = createOutputLabel();
        textPanel.add(encodedTextOut);

        textPanel.add(new JLabel());
        textPanel.add(createCenteredLabel("Enter text to decode here"));
        codedTextIn = new JTextField(50);
        codedTextIn.getDocument().addDocumentListener(new CodedListener());
        textPanel.add(codedTextIn);
        textPanel.add(createCenteredLabel("Decoded text"));
        decodedTextOut = createOutputLabel();
        textPanel.add(decodedTextOut);
        textPanel.add(new JLabel());

        adfgvx = new Adfgvx("");

        JPanel lowerPanel = new JPanel(new GridLayout(1,3));
        lowerPanel.add(new JLabel(""));
        lowerPanel.add(buildLabelPanel());
        lowerPanel.add(new JLabel(""));
        add(textPanel, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);
        add(new JLabel("     "), BorderLayout.WEST);
        add(new JLabel("     "), BorderLayout.EAST);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(30, 30);
        pack();
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.BLUE);
        return label;
    }

    private JLabel createOutputLabel() {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.RED));
        return label;
    }

    private void updateKeyString() {
        adfgvx.setKey(keyText.getText());
        updateCodedString();
        updateDecodedString();
    }

    private void updateCodedString() {
        String line = clearTextIn.getText();
        encodedTextOut.setText(adfgvx.encode(line));
    }

    private void updateDecodedString() {
        String line = codedTextIn.getText();
        decodedTextOut.setText(adfgvx.decode(line));
    }

    private JPanel buildLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 3, 3));
        panel.add(createCenteredLabel("Translation Grid"));

        JLabel tmp = new JLabel("tmp");
        Font regularFont = tmp.getFont();
        Font boldFont = regularFont.deriveFont(regularFont.getStyle(), (int) (regularFont.getSize2D() * 1.25));

        char[] morse = adfgvx.getMorse();
        char[][] grid = adfgvx.getGrid();
        JPanel p = new JPanel(new GridLayout(1,7));
        p.add(new JLabel());
        for(int i = 0; i < morse.length; i++) {
            JLabel label = new JLabel("" + morse[i]);
            label.setFont(boldFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(label);
        }
        panel.add(p);
        for(int i = 0; i < morse.length; i++) {
            p = new JPanel(new GridLayout(1, 7, 3, 3));
            JLabel label = new JLabel("" + morse[i]);
            label.setFont(boldFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(label);
            for(int j = 0; j < 6; j++) {
                label = new JLabel("" + grid[i][j]);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                p.add(label);
            }
            panel.add(p);
        }
        return panel;
    }
    public static void main(String[] args) {
        new AdfgvxGui();
    }

    class CodedListener implements DocumentListener {

        public void changedUpdate(DocumentEvent arg0) {
            updateDecodedString();
        }

        public void insertUpdate(DocumentEvent arg0) {
            updateDecodedString();
        }

        public void removeUpdate(DocumentEvent arg0) {
            updateDecodedString();
        }
    }

    class ClearListener implements DocumentListener {

        public void changedUpdate(DocumentEvent arg0) {
            updateCodedString();
        }

        public void insertUpdate(DocumentEvent arg0) {
            updateCodedString();
        }

        public void removeUpdate(DocumentEvent arg0) {
            updateCodedString();
        }
    }

    class KeyListener implements DocumentListener {

        public void changedUpdate(DocumentEvent arg0) {
            updateKeyString();
        }

        public void insertUpdate(DocumentEvent arg0) {
            updateKeyString();
        }

        public void removeUpdate(DocumentEvent arg0) {
            updateKeyString();
        }

    }

}