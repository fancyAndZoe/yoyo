package yoyocom.fancy.yoyolibrary.tools;

/**
 * Created by fancy on 2016/2/16.
 */
public class YoTaskSingleEnd extends YoTaskSingleBlock {

    private boolean isEnded;

    @Override
    public int getMode() {
        return MODE_SINGLE_TASK_END;
    }

    @Override
    public YoTaskManager addOperation(Operation operation) {
        if(!isEnded){
            super.addOperation(operation);
        }
        return this;
    }

    @Override
    public void execute() {
        if(!isEnded){
            super.execute();
        }
    }

    @Override
    protected void onTaskFinished(Task task, boolean succeed) {
        isEnded = true;
    }
}
