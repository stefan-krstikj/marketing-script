public class Question {
    public int questionNumber;
    public String questionText;
    public String questionAnswer;

    public Question(int questionNumber, String questionText, String questionAnswer) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.questionAnswer = questionAnswer;
    }

    @Override
    public String toString() {
        return questionNumber + ": QUESTION: " + questionText + ",\n\t ANSWER: " + questionAnswer + "\n";
    }
}
