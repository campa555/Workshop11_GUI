import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.WebSocket;
import java.util.Random;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        // frame.setLayout(new FlowLayout());

        // define panels
        JPanel startingPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        JPanel bmiPanel = new JPanel();

        // set layout for each panel
        startingPanel.setLayout(new BorderLayout(5, 15));
        mainPanel.setLayout(new GridLayout(6,2));
        bmiPanel.setLayout(new BorderLayout(5, 5));

        // define a button and a label for the starting panel
        JButton nextPanel = new JButton("Next");
        JLabel welcome = new JLabel("<html>Welcome to BMI Calculator!<br>Please press the button below to proceed.</html>");

        // add objects to startingPanel
        startingPanel.add(welcome, BorderLayout.CENTER);
        startingPanel.add(nextPanel, BorderLayout.SOUTH);

        // define required objects for the mainPanel
        JButton calculateButton = new JButton("Calculate BMI");
        JButton previousPanel = new JButton("Previous");
        JLabel weightLabel = new JLabel("<html>Please enter your weight in Kg:<br></html> ");
        JTextField weightTextField = new JTextField(15);
        JLabel heightLabel = new JLabel("Please enter your height in cm: ");
        JTextField heightTextField = new JTextField(15);
        JLabel dummyLabel = new JLabel("");
        JLabel resultLabel = new JLabel("");

        // add objects to mainPanel
        mainPanel.add(dummyLabel);
        mainPanel.add(weightLabel);
        mainPanel.add(weightTextField);
            mainPanel.add(dummyLabel);
        mainPanel.add(heightLabel);
        mainPanel.add(heightTextField);
        mainPanel.add(dummyLabel);
        mainPanel.add(calculateButton);
        mainPanel.add(resultLabel);

        // add objects to bmiPanel
        bmiPanel.add(mainPanel, BorderLayout.CENTER);
        bmiPanel.add(previousPanel, BorderLayout.SOUTH);

        // add action listeners to each button
        nextPanel.addActionListener(
                new SwitchPanelActionListener(frame, startingPanel, bmiPanel) {}
        );
        previousPanel.addActionListener(
                new SwitchPanelActionListener(frame, bmiPanel, startingPanel) {}
        );
        calculateButton.addActionListener(
                new CalculateActionListener(weightTextField, heightTextField, resultLabel) {}
        );

        frame.add(startingPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setVisible(true);



    }
}

class SwitchPanelActionListener implements ActionListener {
    private JFrame frame;
    private JPanel currentPanel;
    private JPanel nextPanel;

    public SwitchPanelActionListener(JFrame frame, JPanel currentPanel, JPanel nextPanel) {
        this.frame = frame;
        this.currentPanel = currentPanel;
        this.nextPanel = nextPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.remove(currentPanel);
        frame.add(nextPanel);
        frame.revalidate();
        frame.repaint();
    }
}

class CalculateActionListener implements ActionListener {
    private JTextField weightTextField;
    private JTextField heightTextField;
    private double bmi;
    JLabel resultLabel;

    public CalculateActionListener(JTextField weightTextField, JTextField heightTextField, JLabel resultLabel) {
        this.weightTextField = weightTextField;
        this.heightTextField = heightTextField;
        this.resultLabel = resultLabel;
        this.bmi = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bmi = Double.parseDouble(weightTextField.getText())/ Math.pow(Double.parseDouble(heightTextField.getText())/100,2);
        resultLabel.setText("BMI is " + bmi);
        resultLabel.setVisible(true);
    }

    double getBmi() {
        return bmi;
    }
}