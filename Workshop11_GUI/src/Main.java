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
        JPanel bmiCenterPanel = new JPanel(new GridLayout(2, 1));

        // set layout for each panel
        startingPanel.setLayout(new BorderLayout(5, 15));
        mainPanel.setLayout(new GridLayout(5,2));
        bmiPanel.setLayout(new BorderLayout(5, 5));


        // define a button and a label for the starting panel
        JButton nextPanel = new JButton("Next");

        // to align the text at the center of the page, I used SwingConstants.CENTER inside label constructor (did a little bit of searching for that.
        // also to be able to go to the next line, looks like regular '\n' does not work and I have to use <br> in html text format to be able to go to the next line.
        JLabel welcome = new JLabel(
                "<html>Welcome to BMI Calculator!<br>Please press the button below to proceed.</html>",
                SwingConstants.CENTER
        );

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
        JLabel dummyLabel = new JLabel(" ");
        JLabel resultLabel = new JLabel(
                "",
                SwingConstants.CENTER
        );

        // add objects to mainPanel
        mainPanel.add(dummyLabel);
        mainPanel.add(weightLabel);
        mainPanel.add(weightTextField);
        mainPanel.add(dummyLabel);
        mainPanel.add(heightLabel);
        mainPanel.add(heightTextField);
        mainPanel.add(dummyLabel);
        mainPanel.add(calculateButton);
        // mainPanel.add(resultLabel);

        // add objects to bmiPanel
        // the sole use of gridlayout makes the buttons and fields as large as the borders. that makes the GUI ugly in my opinion.
        // to fix that, I've added the main panel to a wrapper style panel and then added it to bmi panel. This reverts the buttons
        // and fields back to their default size.
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(mainPanel);

        bmiCenterPanel.add(wrapper);
        bmiCenterPanel.add(resultLabel);

        bmiPanel.add(bmiCenterPanel, BorderLayout.CENTER);

        bmiPanel.add(previousPanel, BorderLayout.SOUTH);

        // add action listeners to each button
        nextPanel.addActionListener(
                new SwitchPanelActionListener(frame, startingPanel, bmiPanel) {}
        );
        previousPanel.addActionListener(
                new SwitchPanelActionListener(frame, bmiPanel, startingPanel) {}
        );
        calculateButton.addActionListener(
                new CalculateActionListener(weightTextField, heightTextField, resultLabel, bmiCenterPanel) {}
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
    JPanel bmiPanel;

    public CalculateActionListener(JTextField weightTextField, JTextField heightTextField, JLabel resultLabel, JPanel bmiPanel) {
        this.weightTextField = weightTextField;
        this.heightTextField = heightTextField;
        this.resultLabel = resultLabel;
        this.bmi = 0;
        this.bmiPanel = bmiPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bmi = Double.parseDouble(weightTextField.getText())/ Math.pow(Double.parseDouble(heightTextField.getText())/100,2);

        String bmi_text = "";

        // change background color according to bmi measurement.
        int expression = (bmi < 18.5 ? 1 : 0)
                | (bmi < 30 && bmi >= 18.5 ? 2 : 0)
                | (bmi < 40 && bmi >= 30 ? 4 : 0)
                | (bmi > 40 ? 8 : 0);

        switch(expression) {
            case 1:
                bmi_text = "You are underweight";
                this.bmiPanel.setBackground(Color.decode("#1192D1"));
                break;
            case 2:
                bmi_text = "You have a normal weight";
                this.bmiPanel.setBackground(Color.CYAN);
                break;
            case 3:
                bmi_text = "You are overweight";
                this.bmiPanel.setBackground(Color.ORANGE);
                break;
            case 4:
                bmi_text = "You are obese";
                this.bmiPanel.setBackground(Color.RED);
                break;
        }

        // used String.format to show bmi measurement up to 2 decimal digits
        resultLabel.setText( "<html>" + String.format("BMI is %.2f", bmi) + "<br>" + bmi_text + "</html>" );


        resultLabel.setVisible(true);
    }

    double getBmi() {
        return bmi;
    }
}