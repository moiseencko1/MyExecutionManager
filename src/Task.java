/**
 * Created by Acer on 28.11.2016.
 */
public class Task implements Runnable {
    private final String message;
    private final long millis;
    private final boolean isException;

    public Task(String message, long millis, boolean isException) {
        this.message = message;
        this.millis = millis;
        this.isException = isException;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((isException ? "Error" : "Task") + " Message: " + message);
        if (isException) throw new RuntimeException();

    }
}
