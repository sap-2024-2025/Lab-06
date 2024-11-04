package sap.pixelart.dashboard.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BootDialog extends JDialog {

    private JTextField gridId;
    private JTextField errorField;

    public BootDialog(Frame parent) {
        super(parent, ".:: Cooperative Pixel Art ::.", true);
        setLayout(new BorderLayout());

        // Main panel with label and text field
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel gridPanel = new JPanel();
        gridPanel.add(new JLabel("Grid Id:"));
        gridId = new JTextField(15);
        gridId.setText("my-grid-01");
        gridPanel.add(gridId);
        mainPanel.add(gridPanel);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gridName = gridId.getText();
                if (gridName.isEmpty()) {
                    errorField.setText("Grid Name cannot be empty.");
                } else {
                    errorField.setText(""); // Clear error field
                    dispose(); // Close the dialog
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        errorField = new JTextField();
        errorField.setEditable(false);
        errorField.setForeground(Color.RED);
        mainPanel.add(errorField);

        setSize(300, 150);
        add(mainPanel);
        setLocationRelativeTo(parent);
    }

    public String getGridId() {
    	return gridId.getText();
    }
    
    public static void main(String[] args) {
        JFrame parentFrame = new JFrame();
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentFrame.setSize(400, 300);
        parentFrame.setVisible(true);

        BootDialog dialog = new BootDialog(parentFrame);
        dialog.setVisible(true);
    }
}
