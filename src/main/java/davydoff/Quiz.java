package davydoff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class Quiz {

    private static final Logger QUIZ_LOGGER = LoggerFactory.getLogger("Quiz");

    private final String url;

    private final Gson gson;

    private int correctAnswers;
    private int wrongAnswers;

    public Quiz(String url) {
        this.url = url;

        gson = new Gson();

        correctAnswers = 0;
        wrongAnswers = 0;
    }

    public List<Question> getQuestions() throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            QUIZ_LOGGER.info("Connected to url");

            int response = connection.getResponseCode();
            QUIZ_LOGGER.info("Response code: " + response);

            if (response == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                    return gson.fromJson(reader.lines().collect(Collectors.joining()),
                            new TypeToken<List<Question>>() {
                            }.getType());

                }
            }

            throw new RuntimeException("Bad response from server. Response code: " + response);

        } catch (Exception e) {
            QUIZ_LOGGER.error("Connection failed: ", e);
            throw e;
        }
    }

    public void startQuiz() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            QUIZ_LOGGER.info("Quiz started");

            System.out.println("----------Welcome to quizzer!----------");
            System.out.println("Commands:");

            System.out.println("/start - start the quiz or get more questions.");
            System.out.println("/quit - quit the quiz.\n");

            System.out.print("Enter command: ");

            while (true) {
                switch (reader.readLine().trim().toLowerCase()) {
                    case "/start" -> {
                        List<Question> questions = getQuestions();
                        for (Question q : questions) {
                            System.out.println(q.question);
                            System.out.print("Enter your answer: ");
                            if (reader.readLine().trim().equalsIgnoreCase(q.answer)) {
                                System.out.println("Correct!\n");
                                ++correctAnswers;
                            } else {
                                System.out.println("Wrong! Correct answer: " + q.answer);
                                ++wrongAnswers;
                            }
                            System.out.println();
                        }
                        System.out.print("Enter command: ");
                    }
                    case "/quit" -> {
                        System.out.println("Correct answers: " + correctAnswers);
                        System.out.println("Wrong answers: " + wrongAnswers);
                        System.out.println("Good game!");
                        QUIZ_LOGGER.info("Quiz ended");
                        return;
                    }
                    default -> {
                        System.out.println("No such command!\n");
                        System.out.print("Enter command: ");
                    }
                }
            }

        } catch (IOException e) {
            QUIZ_LOGGER.error("Occured IOException in startQuiz(): ", e);
            throw new RuntimeException(e);
        }
    }
}
