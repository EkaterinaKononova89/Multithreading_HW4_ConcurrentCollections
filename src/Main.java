import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> letterA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> letterB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> letterC = new ArrayBlockingQueue<>(100);
    static int aMax = 0;
    static  String maxLineA = null;
    static int bMax = 0;
    static  String maxLineB = null;
    static int cMax = 0;
    static  String maxLineC = null;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    letterA.put(text); // можно в один трай-кетч или лучше для каждого свой?
                    letterB.put(text);
                    letterC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        // а
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String line;
                int countA;
                try {
                    line = letterA.take();
                    countA = countLettersInOneLine(line, 'a');
                } catch (InterruptedException e) {
                    return;
                }
                if (countA > aMax) {
                    aMax = countA;
                    maxLineA = line;
                }
            }
        });
        threadA.start();

        // b
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String line;
                int countB;
                try {
                    line = letterB.take();
                    countB = countLettersInOneLine(line, 'b');
                } catch (InterruptedException e) {
                    return;
                }
                if (countB > bMax) {
                    bMax = countB;
                    maxLineB = line;
                }
            }
        });
        threadB.start();

        // c
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String line;
                int countC;
                try {
                    line = letterC.take();
                    countC = countLettersInOneLine(line, 'c');
                } catch (InterruptedException e) {
                    return;
                }
                if (countC > cMax) {
                    cMax = countC;
                    maxLineC = line;
                }
            }
        });
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Максимальное кол-во 'а' равно " + aMax + " и содержится в строке: \n" + maxLineA); // отображает
                                                                // только первую встретившуюся строку с максимальным кол-вом
        System.out.println("Максимальное кол-во 'b' равно " + bMax + " и содержится в строке: \n" + maxLineB);
        System.out.println("Максимальное кол-во 'c' равно " + cMax + " и содержится в строке: \n" + maxLineC);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countLettersInOneLine(String line, char c) throws InterruptedException {
        int a = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) == c) {
                a++;
            }
        }
        return a;
    }
}
