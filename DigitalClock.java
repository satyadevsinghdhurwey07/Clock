import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Digital Clock - Java Swing
 * Features: live time, date, 12/24-hour toggle, simple alarm.
 */
public class DigitalClock extends JFrame {

    private final JLabel timeLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel alarmStatus = new JLabel("No alarm set", SwingConstants.CENTER);

    private boolean is24Hour = false;
    private LocalTime alarmTime = null;
    private boolean alarmRinging = false;

    private final DateTimeFormatter fmt12 = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter fmt24 = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    public DigitalClock() {
        setTitle("Digital Clock");
        setSize(480, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.BLACK);

        // Time label
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 56));
        timeLabel.setForeground(new Color(0, 255, 120));

        // Date label
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        dateLabel.setForeground(Color.WHITE);

        // Centre panel (time + date)
        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setBackground(Color.BLACK);
        center.add(timeLabel);
        center.add(dateLabel);
        add(center, BorderLayout.CENTER);

        // Bottom panel (buttons + alarm status)
        JButton toggleBtn = new JButton("12/24 Hour");
        JButton alarmBtn = new JButton("Set Alarm");
        JButton clearBtn = new JButton("Clear Alarm");

        toggleBtn.addActionListener(e -> is24Hour = !is24Hour);
        alarmBtn.addActionListener(e -> setAlarm());
        clearBtn.addActionListener(e -> {
            alarmTime = null;
            alarmRinging = false;
            alarmStatus.setText("No alarm set");
        });

        alarmStatus.setForeground(Color.YELLOW);

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.BLACK);
        buttons.add(toggleBtn);
        buttons.add(alarmBtn);
        buttons.add(clearBtn);

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.setBackground(Color.BLACK);
        bottom.add(alarmStatus);
        bottom.add(buttons);
        add(bottom, BorderLayout.SOUTH);

        // Timer: updates the clock every 1000 ms
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
        updateClock();
    }

    private void updateClock() {
        LocalTime now = LocalTime.now();
        timeLabel.setText(now.format(is24Hour ? fmt24 : fmt12));
        dateLabel.setText(LocalDate.now().format(dateFmt));

        // Alarm check (compare hour and minute only)
        if (alarmTime != null && !alarmRinging
                && now.getHour() == alarmTime.getHour()
                && now.getMinute() == alarmTime.getMinute()) {
            alarmRinging = true;
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Alarm! It's " + alarmTime, "Alarm",
                    JOptionPane.INFORMATION_MESSAGE);
            alarmTime = null;
            alarmRinging = false;
            alarmStatus.setText("No alarm set");
        }
    }

    private void setAlarm() {
        String input = JOptionPane.showInputDialog(this, "Enter alarm time in HH:mm (24-hour format):");
        if (input == null) return; // user cancelled
        try {
            alarmTime = LocalTime.parse(input.trim());
            alarmStatus.setText("Alarm set for " + alarmTime);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid time. Use format like 07:30 or 18:45.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DigitalClock().setVisible(true));
    }
}
