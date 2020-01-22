import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    JFrame frame;
    JButton go;
    JButton vouvou;
    static Action action;
    JComboBox resourses;

    public static void main(String[] args) {
        new GUI();
        action = new Action();
    }

    public GUI() {
        frame = new JFrame("GameBot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(170,70);
        frame.setResizable(false);
        frame.setLocation(1140,520);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        go = new JButton("Старт");//new ImageIcon("E:\\temp\\start.png"));
        vouvou = new JButton("Пауза");//new ImageIcon("E:\\temp\\stop.png"));
        ActionListener piu = new GoActionListener();
        ActionListener stope = new VouvouActionListener();
        go.addActionListener(piu);
        vouvou.addActionListener(stope);
        vouvou.setEnabled(false);

        frame.getContentPane().add(panel);
        panel.add(go);
        panel.add(vouvou);

        frame.setVisible(true);
    }

    public class GoActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            go.setEnabled(false);
            vouvou.setEnabled(true);

            Thread thread = new Thread(() -> {

                action.setPause(true);
                try {
                    action.searchResourses();
                } catch (AWTException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });
            thread.start();
        }
    }

    public class VouvouActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            vouvou.setEnabled(false);
            go.setEnabled(true);
            {
                action.setPause(false);
            }
        }
    }
}