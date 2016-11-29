/**
 * Created by Acer on 27.11.2016.
 */
public class ExecutionManagerImpl implements ExecutionManager {

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        return new ContextImpl(callback, tasks);
    }
}
