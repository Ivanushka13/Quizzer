package davydoff;

public class Main {

    private static final String URL = "https://jservice.io/api/random?count=2";

    public static void main(String[] args) {

        Quiz quiz = new Quiz(URL);

        quiz.startQuiz();
    }
}
