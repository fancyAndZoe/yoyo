package yoyocom.fancy.yoyolibrary.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoTaskMulti extends YoTaskManager {
    private List<Task> taskList = new ArrayList<Task>();
    private Task curTask;

    @Override
    public int getMode() {
        return MODE_MULTI_TASK;
    }

    @Override
    public boolean hasTaskRunning() {
        return !taskList.isEmpty();
    }

    @Override
    public void cancelAll() {
        for (Task task : taskList) {
            task.cancel(true);
        }

        taskList.clear();
    }

    @Override
    public YoTaskManager addOperation(Operation operation) {
        curTask = new Task(operation);
        taskList.add(curTask);
        return this;
    }

    @Override
    public void setNextParamsToNextTask(Object... params) {

    }

    @Override
    public void execute() {
        if (curTask != null) {
            curTask.execute(curTask.operation.getParams());
        }
    }

    @Override
    protected void onTaskPreExecute(Task task) {

    }

    @Override
    protected void onTaskEnd(Task task) {
        try {
            taskList.remove(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onTaskFinished(Task task, boolean succeed) {

    }
}
