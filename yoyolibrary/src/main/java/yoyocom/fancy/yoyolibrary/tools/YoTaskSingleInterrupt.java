package yoyocom.fancy.yoyolibrary.tools;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoTaskSingleInterrupt extends YoTaskManager {
    private Task task;

    @Override
    public int getMode() {
        return MODE_SINGLE_TASK_INTERRUPT;
    }

    @Override
    public boolean hasTaskRunning() {
        return task != null && !task.isEnded;
    }

    @Override
    public void cancelAll() {
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public YoTaskManager addOperation(Operation operation) {
        if (hasTaskRunning()) {
            cancelAll();
        }

        task = new Task(operation);
        return this;
    }

    @Override
    public void setNextParamsToNextTask(Object... params) {

    }

    @Override
    public void execute() {
        if (task != null && !task.isStarted) {
            task.execute(task.operation.getParams());
        }
    }

    @Override
    protected void onTaskPreExecute(Task task) {

    }

    @Override
    protected void onTaskEnd(Task task) {

    }

    @Override
    protected void onTaskFinished(Task task, boolean succeed) {

    }
}
