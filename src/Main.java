/**
 * Created by Acer on 28.11.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutionManager executionManager = new ExecutionManagerImpl();
        Runnable callback = new Task("callback", 0, false);
        Runnable[] tasks = {new Task("1", 300, true), new Task("2", 500, false), new Task("3", 0, false),
                new Task("4", 2, false), new Task("5", 5, false), new Task("6", 6, false)};
        Context execute = executionManager.execute(callback, tasks);

        Thread.sleep(1);
        execute.interrupt();
        Thread.sleep(1000);
        System.out.println(execute.isFinished());
        System.out.println(execute.getFailedTaskCount());
        System.out.println(execute.getCompletedTaskCount());
        System.out.println(execute.getInterruptedTaskCount());


    }
}
