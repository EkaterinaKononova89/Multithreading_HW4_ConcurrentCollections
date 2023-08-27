import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> letterA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> letterB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> letterC = new ArrayBlockingQueue<>(100);

    static final int AMOUNT_OF_TEXTS = 10_000;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < AMOUNT_OF_TEXTS; i++) {
                String text = generateText("abc", 100_000);
                try {
                    letterA.put(text);
                    letterB.put(text);
                    letterC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        // а
        new Thread(() -> {
            try {
                countLettersInThread(AMOUNT_OF_TEXTS, letterA, 'a');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // b
        new Thread(() -> {
            try {
                countLettersInThread(AMOUNT_OF_TEXTS, letterB, 'b');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // c
        new Thread(() -> {
            try {
                countLettersInThread(AMOUNT_OF_TEXTS, letterC, 'c');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void countLettersInThread (int amountOfTexts, BlockingQueue<String> letter, char c) throws InterruptedException {
        String lineMax = null;
        int charMax = 0;
        for (int i = 0; i < amountOfTexts; i++) {
            String line;
            int countChar = 0;
            line = letter.take();
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == c) {
                    countChar++;
                }
            }
            if (countChar > charMax) {
                charMax = countChar;
                lineMax = line;
            }
        }
        System.out.println("Максимальное кол-во '" + c +"' равно " + charMax + " и содержится в строке: \n" + lineMax); // отображает
        // только первую встретившуюся строку с максимальным кол-вом
    }
}
