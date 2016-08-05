package yoyocom.fancy.yoyolibrary.tools;

/**
 * Created by fancy on 2016/2/16.
 * 同一时刻只允许一个异步任务执行，
 * 任务执行时拦截新加入任务
 */
public class YoTaskSingleBlock extends YoTaskManager {

    protected Task task;

    @Override
    public int getMode() {
        return MODE_SINGLE_TASK_BLOCK;
    }

    @Override
    public boolean hasTaskRunning() {
        return null != task && !task.isEnded;
    }

    @Override
    public void cancelAll() {
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public YoTaskManager addOperation(Operation operation) {
        if (!hasTaskRunning()) {
            task = new Task(operation);
        }
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
        this.task = null;
    }

    @Override
    protected void onTaskFinished(Task task, boolean succeed) {

    }
}
