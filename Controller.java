package Simon;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.*;

public class Controller {

    @FXML
    private Button btn1, btn2, btn3, btn4, btnStart, btnMenu, btnReset, btnLoseMenu, btnRetry, btnExit;
    private Button[] btn;

    @FXML
    private Label Score, Title, ScoreLabel, gameOver;

    private int score;
    private String quest;
    private String answer;
    private String pressedButton;

    Random rn = new Random();

    public void start() {


        btn = new Button[]{btn1, btn2, btn3, btn4};
        for (Button b : btn) {
            b.setVisible(true);
        }
        btnMenu.setVisible(true);
        btnReset.setVisible(true);
        btnStart.setVisible(false);
        Score.setVisible(true);
        ScoreLabel.setVisible(true);
        Title.setVisible(false);
        gameOver.setVisible(false);
        btnExit.setVisible(false);
        btnLoseMenu.setVisible(false);
        btnRetry.setVisible(false);

        reset();
    }

    public void menu() {

        btn = new Button[]{btn1, btn2, btn3, btn4};
        for (Button b : btn) {
            b.setVisible(false);
        }
        btnMenu.setVisible(false);
        btnReset.setVisible(false);
        gameOver.setVisible(false);
        btnRetry.setVisible(false);
        btnLoseMenu.setVisible(false);
        btnStart.setVisible(true);
        btnExit.setVisible(true);
        Score.setVisible(false);
        ScoreLabel.setVisible(false);
        Title.setVisible(true);

    }

    private void lose() {
        menu();
        Title.setVisible(false);
        gameOver.setVisible(true);
        btnRetry.setVisible(true);
        btnLoseMenu.setVisible(true);
        btnStart.setVisible(false);

    }

    public void exit() {
        System.exit(0);
    }

    public void reset() {
        score = 0;
        Score.setText("0");
        quest = "";
        answer = "";
        blink(score);
    }

    private void blink(int score) {
        answer = "";
        quest = "";
        Score.setText(Integer.toString(score));
        btn = new Button[]{btn1, btn2, btn3, btn4};

        for (Button b : btn) {
            b.setDisable(true);
        }

        for (int i = 0; i < score + 1; i++) {
            int d = rn.nextInt(4) + 1;
            quest += d;

            Timeline blink = new Timeline(
                    new KeyFrame(
                            Duration.millis((i + 1) * 1000),
                            ae -> {
                                new Timeline(
                                        new KeyFrame(Duration.seconds(0), new KeyValue(btn[d - 1].opacityProperty(), .1)),
                                        new KeyFrame(Duration.seconds(0.5), new KeyValue(btn[d - 1].opacityProperty(), 1))
                                ).play();
                                playSound(Integer.toString(d));
                            }
                    )
            );
            blink.setCycleCount(1); //Ограничим число повторений
            blink.play(); //Запускаем

        }

        Timeline simonTurn = new Timeline(
                new KeyFrame(
                        Duration.millis((score + 1) * 1000),
                        ae -> {
                            new Timeline(
                            ).play();
                            for (Button b : btn) {
                                b.setDisable(false);
                            }
                        }
                )
        );
        simonTurn.setCycleCount(1);
        simonTurn.play();

    }

    public void check(ActionEvent e) {
        Button btn = (Button) e.getSource();
        pressedButton = btn.getText();
        answer += pressedButton;
        playSound(pressedButton);

        if (!answer.equals(quest.substring(0, answer.length()))) {
            lose();
        } else if (answer.length() == quest.length()) {
            score++;
            blink(score);
        }
    }

    private void playSound(String spath) {
        URL path = getClass().getResource("sounds/" + spath + ".wav");
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(path));
            clip.start();

        } catch (Exception e) {
            System.out.println("No sound");
        }
    }
}



