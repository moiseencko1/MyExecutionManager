import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Acer on 27.11.2016.
 */
public class ContextImpl implements Context {
    private final List<Thread> tasksList = new ArrayList<>();
    private volatile boolean isInterrupt;
    private boolean isFinished;
    private final TaskStatus taskStatus = new TaskStatus();

    private class TaskStatus {
        public int taskStatus[] = new int[3];//0-inter, 1-comple 2 -failed
        private int count;

        public int getCount() {
            return count;
        }

        public void increment(int index) {
            taskStatus[index]++;
            count++;
        }

        public void set(int index, int count) {
            taskStatus[index] = count;
            this.count = +count;
        }

        public int get(int index) {
            return taskStatus[index];
        }
    }

    public ContextImpl(Runnable callback, Runnable... tasks) {
        new Thread(() -> {
            for (int i = 0; i < tasks.length; i++) {
                Thread thread = new Thread(tasks[i]);
                thread.setUncaughtExceptionHandler((t, e) -> {
                    synchronized (ContextImpl.class) {
                        tasksList.remove(t);
                    }
                    taskStatus.increment(2);
                });
                if (!isInterrupt) {
                    thread.start();
                    synchronized (ContextImpl.class) {
                        tasksList.add(thread);
                    }
                } else {
                    taskStatus.set(0, tasks.length - i);
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            while ((taskStatus.getCount()) != tasks.length) {
                synchronized (ContextImpl.class) {
                    Iterator<Thread> iterator = tasksList.iterator();
                    while (iterator.hasNext()) {
                        Thread thread = iterator.next();
                        if (thread.getState() == Thread.State.TERMINATED) {
                            iterator.remove();
                            taskStatus.increment(1);
                        }
                    }
                }
            }
            isFinished = true;
            callback.run();
        }).start();
    }

    @Override
    public int getCompletedTaskCount() {
        return taskStatus.get(1);
    }

    @Override
    public int getFailedTaskCount() {
        return taskStatus.get(2);
    }

    @Override
    public int getInterruptedTaskCount() {
        return taskStatus.get(0);
    }

    @Override
    public void interrupt() {
        isInterrupt = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
