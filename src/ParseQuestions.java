import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseQuestions {

    public static List<Question> getQuestionsFromFiles(List<String> fileNames) throws IOException {
        List<Question> questions = new ArrayList<>();
        fileNames
                .stream()
                .forEach(it -> {
                    try {
                        questions.addAll(getQuestionsFromFile(it));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return questions;
    }

    public static List<Question> getQuestionsFromFile(String filePath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = "";
        List<Question> questionList = new ArrayList<>();
        while ((line = br.readLine()) != null || (line != null && isBeginningOfQuestion(line))) {
            // citaj dodeka ne naides na prvata moznost A, B, C ili citaj dodeka ne naides na 'Odgovor: "
            if (!isBeginningOfQuestion(line))
                continue;
            String questionBuffer = line;
            String answer = "";
            while ((line = br.readLine()) != null && !isBegnningOfAnswer(line)) {
                questionBuffer += " " + line;
            }

            if (isBegnningOfAnswer(line)) {
                answer = line;
            } else {
                while ((line = br.readLine()) != null && !isBegnningOfAnswer(line)) {
                }
            }
            answer = line;

            int questionNo = getQuestionNo(questionBuffer);
            Question q = new Question(questionNo, fixQuestionTranslation(questionBuffer), answer);
            questionList.add(q);
        }
        return questionList;
    }

    public static boolean isNumber(Character c) {
        return Character.isDigit(c);
    }

    public static boolean isBeginningOfQuestion(String line) {
        if (line.length() < 2)
            return false;
        return (isNumber(line.charAt(0)) && line.charAt(1) == ')') || (isNumber(line.charAt(0)) && isNumber(line.charAt(1)) && line.charAt(2) == ')')
                || (isNumber(line.charAt(0)) && isNumber(line.charAt(1)) && isNumber(line.charAt(2)) && line.charAt(3) == ')');
    }

    public static boolean isBeginningOfQuestionOptions(String line) {
        if (line == null || line.length() < 2)
            return false;
        return Character.isAlphabetic(line.charAt(0)) && line.charAt(1) == ')';
    }

    public static boolean isBegnningOfAnswer(String line) {
        if (line.length() < 4)
            return false;
        return line.substring(0, 4).equals("Одго");
    }

    public static int getQuestionNo(String question) {
        if ((question.length() > 2) && Character.isDigit(question.charAt(0)) && Character.isAlphabetic(question.charAt(1))) {
            return Integer.parseInt(question.substring(0, 1));
        } else if ((question.length() > 3) && Character.isDigit(question.charAt(0)) && Character.isDigit(question.charAt(1)) && Character.isAlphabetic(question.charAt(2))) {
            return Integer.parseInt(question.substring(0, 2));
        } else if ((question.length() > 4) && Character.isDigit(question.charAt(0)) && Character.isDigit(question.charAt(1)) && Character.isDigit(question.charAt(2))
                && Character.isAlphabetic(question.charAt(3))) {
            return Integer.parseInt(question.substring(0, 3));
        } else return 0;
    }

    public static String fixQuestionTranslation(String question) {
        question = question
                .replace("ИСКЛУЧЕН", "ОСВЕН");
        return question;
    }
}
