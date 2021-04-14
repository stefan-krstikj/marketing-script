import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ConsoleProgram {
    public static Integer LIMIT_ANSWERS = 5;
    public static List<String> questionFiles;
    public static BufferedReader br;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        questionFiles = new ArrayList<>();
        startSetup();

        System.out.println("Looking for: " + questionFiles);

        List<Question> questions = ParseQuestions.getQuestionsFromFiles(questionFiles);
        while (true) {
            System.out.println(AnsiColors.ANSI_PURPLE +
                    "------------------------------------------------------------------------------------------" + AnsiColors.ANSI_RESET);
            System.out.print(AnsiColors.ANSI_RED + "Query: " + AnsiColors.ANSI_RESET);
            String query = br.readLine();
            System.out.println();
            searchQuestions(questions, query);
        }
    }

    public static void startSetup() throws IOException {
        String line;

        System.out.print("Do you want to enter custom files (default: questions.txt and questions-translated.txt)? [yes/no]: ");
        line = br.readLine();
        if (line.toLowerCase().equals("yes") || line.toLowerCase().equals("y") || line.toLowerCase().equals("ye")) {
            System.out.println("Enter files names to look for questions. Enter nothing to finish");
            System.out.print("File path: ");
            while ((line = br.readLine()) != null && line.length() > 0) {
                questionFiles.add(line);
                System.out.print("File path: ");
            }
        } else {
            questionFiles.add("questions.txt");
            questionFiles.add("questions-translated.txt");
        }

        System.out.print("Limit results (default 5): ");
        line = br.readLine();
        if (line != null && line.length() > 0) {
            try {
                Integer newLimit = Integer.parseInt(line);
                LIMIT_ANSWERS = newLimit;
            } catch (Exception e) {
                System.out.println("Did not receive number, using default");
            }
        }

    }

    public static void searchQuestions(List<Question> questions, String query) {
        HashMap<Question, Integer> map = new HashMap<>();
        for (Question q : questions) {
            Integer hits = 0;
            String[] queryWords = query.toLowerCase().split(" ");
            List<String> questionTextWords = Arrays.asList(q.questionText.toLowerCase().split(" "));
            for (String s : queryWords) {
                if (questionTextWords.contains(s))
                    hits++;
            }
            map.put(q, hits);

        }
        map.entrySet()
                .stream()
                .filter(it -> it.getValue() > 0)
                .sorted((Map.Entry.<Question, Integer>comparingByValue().reversed()))
                .limit(LIMIT_ANSWERS)
                .forEach(it -> {
                    System.out.println("HITS: " + it.getValue());
                    printQuestionToConsole(it.getKey());
                });
        System.out.println();
    }

    public static void printQuestionToConsole(Question q) {
        List<String> questionWords = Arrays.asList(q.questionText.split(" "));
        for (int i = 0; i < questionWords.size() - 1; i++) {
            System.out.print(questionWords.get(i) + " ");
            if (questionWords.get(i + 1).length() >= 2 && questionWords.get(i + 1).charAt(1) == ')') {
                System.out.println("");
            }
        }
        System.out.println(questionWords.get(questionWords.size() - 1));
        System.out.println("\t\t" + q.questionAnswer + "\n");
    }
}
