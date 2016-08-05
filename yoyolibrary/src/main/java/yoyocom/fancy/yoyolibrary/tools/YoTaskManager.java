package yoyocom.fancy.yoyolibrary.tools;

import android.os.AsyncTask;

/**
 * Created by fancy on 2016/2/16.
 */
public abstract class YoTaskManager {

    public static final int MODE_SINGLE_TASK_BLOCK = 0;

    public static final int MODE_SINGLE_TASK_INTERRUPT = 1;

    public static final int MODE_SINGLE_TASK_END = 2;

    public static final int MODE_MULTI_TASK = 3;

    public static final int MODE_MULTI_TASK_QUEUE = 4;

    /**返回当前taskManager的任务队列运行模式*/
    public abstract int getMode();
    /**是否有异步任务正在执行*/
    public abstract boolean hasTaskRunning();
    /**取消正在执行的任务，清除任务队列*/
    public abstract void cancelAll();
    /**向任务队列里添加一个任务，不同的mode会有不同结果*/
    public abstract YoTaskManager addOperation(Operation operation);
    /**为任务队列里下一个任务设置参数，只有mode为MODE_MULTI_TASK_QUEUE时才起效*/
    public abstract void setNextParamsToNextTask(Object...params);
    /**执行任务队列里的任务*/
    public abstract void execute();
    /**某个任务启动时触发*/
    protected abstract void onTaskPreExecute(Task task);
    /**某个任务结束(正常结束或者被取消)时触发*/
    protected abstract void onTaskEnd(Task task);
    /**某个任务正常结束时触发，用来通知任务队列执行下一个任务*/
    protected abstract void onTaskFinished(Task task, boolean succeed);

    public YoTaskManager() {}

    /**
     * 得到不同类型的yoTaskManager实例
     * @param mode
     * @return
     */
    public static YoTaskManager getInstance(int mode) {
        switch (mode) {
            case MODE_SINGLE_TASK_BLOCK:
                return new YoTaskSingleBlock();
            case MODE_SINGLE_TASK_INTERRUPT:
                return new YoTaskSingleInterrupt();
            case MODE_SINGLE_TASK_END:
                return new YoTaskSingleEnd();
            case MODE_MULTI_TASK:
                return new YoTaskMulti();
            case MODE_MULTI_TASK_QUEUE:
                return new YoTaskMultiQueue();
            default:
                return new YoTaskSingleBlock();
        }
    }

    protected class Task extends AsyncTask<Object, Object, Object> {
        final Operation operation;
        boolean isStarted;
        boolean isEnded;

        public Task(Operation operation) {
            this.operation = operation;
        }

        @Override
        protected void onPreExecute() {
            isStarted = true;
            onTaskPreExecute(this);
            //优先获取一次缓存
            operation.onTaskPreExecute();
        }

        @Override
        protected void onCancelled() {
            isEnded = true;
            onTaskEnd(this);
            operation.onTaskCancel();
        }

        @Override
        protected void onCancelled(Object result) {
            isEnded = true;
            onTaskEnd(this);
            operation.onTaskCancel();
        }

        @Override
        protected Object doInBackground(Object... params) {
            return operation.onBackgroundTask(params);
        }

        @Override
        protected void onPostExecute(Object result) {
            isEnded = true;
            onTaskEnd(this);
            onTaskFinished(this, operation.onPostExecute((ResponseResult)result));
        }
    };//end of Task
}
