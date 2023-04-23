package davydoff;

import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("question")
    public final String question;

    @SerializedName("answer")
    public final String answer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

}
