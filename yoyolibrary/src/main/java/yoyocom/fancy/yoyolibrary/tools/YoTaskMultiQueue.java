package yoyocom.fancy.yoyolibrary.tools;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoTaskMultiQueue extends YoTaskManager {
    private final Queue<Task> taskQueue = new LinkedList<Task>();
    private Task curRunningTask;

    @Override
    public int getMode() {
        return MODE_MULTI_TASK_QUEUE;
    }

    @Override
    public boolean hasTaskRunning() {
        return !taskQueue.isEmpty() && curRunningTask != null;
    }

    @Override
    public void cancelAll() {
        if (curRunningTask != null) {
            curRunningTask.cancel(true);
        }

        while (!taskQueue.isEmpty()) {
            taskQueue.poll().cancel(true);
        }

        taskQueue.clear();
        curRunningTask = null;
    }

    @Override
    public YoTaskManager addOperation(Operation operation) {
        taskQueue.offer(new Task(operation));
        return this;
    }

    @Override
    public void setNextParamsToNextTask(Object... params) {
        Task next = taskQueue.peek();
        if (next != null) {
            next.operation.setParams(params);
        }
    }

    @Override
    public void execute() {
        if (curRunningTask == null) {
            curRunningTask = taskQueue.poll();
            if (curRunningTask != null) {
                curRunningTask.execute(curRunningTask.operation.getParams());
            }
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
        if (succeed && !taskQueue.isEmpty()) {
            curRunningTask = taskQueue.poll();
            if (curRunningTask != null) {
                curRunningTask.execute(curRunningTask.operation.getParams());
            }
        } else {
            cancelAll();
        }
    }
}
