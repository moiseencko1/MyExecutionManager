/**
 * Created by Acer on 27.11.2016.
 */
public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);

}
