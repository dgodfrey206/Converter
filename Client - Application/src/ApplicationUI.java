import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ApplicationUI extends JFrame {

    JTextPane resArea;
    JTextField nameField;
    JComboBox<String> categoriesList;
    JCheckBox searchByCategory;
    JCheckBox searchByName;

    JButton searchBtn;

    public ApplicationUI() {
        super("Search Companies");
        setupGUI();

        // disconnects when close button is clicked
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ConnectionController.getInstance().disconnect();
                } catch (IOException ex) {
                } finally {
                    System.exit(0);
                }
            }
        });
    }

    public void sendRequest() {
        if(!searchByName.isSelected() && !searchByCategory.isSelected())
            return;
        if (searchByCategory.isSelected() && categoriesList.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String msg = "";
            if (searchByCategory.isSelected()) {
                msg = "searchByCategory";
                Connection.out.writeUTF(msg);
                Connection.out.writeUTF(categoriesList.getSelectedItem().toString());
            } else if (searchByName.isSelected()) {
                msg = "searchByName";
                Connection.out.writeUTF(msg);
                Connection.out.writeUTF(nameField.getText());
            }
        } catch (IOException e) {
        }
    }

    public void receiveMessage(String message) throws IOException {
        if (message.equals("categories")) {
            message = Connection.in.readUTF();
            String[] categories = message.split(",");
            for (String category : categories) {
                if (!category.equals(""))
                    categoriesList.addItem(category);
            }
        } else {
            setResponse(message, Color.BLACK);
        }
    }

    private void setResponse(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        resArea.setCharacterAttributes(aset, false);
        resArea.setText(msg);
    }

    private void setupGUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, 200, 300);

        //frame.getContentPane()
        resArea = new JTextPane();
        resArea.setBorder(new EmptyBorder(new Insets(10, 20, 10, 20)));
        resArea.setMargin(new Insets(5, 5, 5, 5));
        resArea.setFocusable(false);

        nameField = new JTextField();
        nameField.setMargin(new Insets(2, 2, 2, 2));
        nameField.setBounds(10, 10, 200, 30);
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // check if an alphabet is pressed
                if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
                    if (searchByName.isSelected()) {
                        searchCompaniesAsync(nameField.getText());
                    }
                }
            }
         });


        categoriesList = new JComboBox<>();
        categoriesList.addItem("-- Select --");
        categoriesList.setBounds(220, 10, 200, 30);


        searchByName = new JCheckBox("Search by name");
        searchByName.setBounds(430, 15, 150, 20);
        searchByName.setSelected(true);
        categoriesList.setEnabled(false);
        searchByName.addActionListener(e -> {
            if (searchByName.isSelected()) {
                nameField.setEnabled(true);
                categoriesList.setEnabled(false);
                searchByCategory.setSelected(false);
            }
        });

        searchByCategory = new JCheckBox("Search by category");
        searchByCategory.setBounds(580, 15, 140, 20);
        searchByCategory.setSelected(false);
        searchByCategory.addActionListener(e -> {
            if (searchByCategory.isSelected()) {
                categoriesList.setEnabled(true);
                nameField.setEnabled(false);
                searchByName.setSelected(false);
            }
        });


        JScrollPane scrollpane = new JScrollPane(resArea);
        panel.add(scrollpane, BorderLayout.CENTER);

        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            sendRequest();
        });

        setLayout(null);
        searchBtn.setBounds(740, 10, 90, 30);
        panel.setBounds(10, 50, 820, 500);
        resArea.setBounds(0, 0, 820, 500);
        Container container = getContentPane();
        container.add(panel);
        container.add(searchBtn);
        container.add(nameField);
        container.add(categoriesList);
        container.add(searchByName);
        container.add(searchByCategory);


        setSize(860, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void searchCompaniesAsync(String pattern) {
        new Thread(() -> {
            try {
                Connection.out.writeUTF("searchByName");
                Connection.out.writeUTF(pattern);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startChat() {
        try {
            while (!Connection.aSocket.isClosed()) {
                receiveMessage(Connection.in.readUTF());
            }
        } catch (IOException e) {
            try {
                ConnectionController.getInstance().disconnect();
            } catch (IOException ignored) {}
        }
    }
}

