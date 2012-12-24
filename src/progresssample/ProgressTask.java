package progresssample;

import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;

/**
 * Task for ProgressBar.
 * 
 * @author tomo
 */
public class ProgressTask extends Task<String> {
    private String taskName;
    private int numberOfTask;

    public ProgressTask(String taskName, int numberOfTask) {
        this.taskName = taskName;
        this.numberOfTask = numberOfTask;
    }

    @Override
    protected String call() throws Exception {
        updateMessage("started");
        int i;
        for (i = 1; i <= numberOfTask; i++) {
            if (isCancelled()) {
                updateMessage("??? cancelled ???");
                break;
            }
            updateProgress(i, numberOfTask);
            TimeUnit.SECONDS.sleep(1);
            updateMessage(String.format("running %d/%d", i, numberOfTask));
        }
        updateMessage("done");
        return "Done";
    }

    @Override
    protected void updateMessage(String string) {
        super.updateMessage(taskName + " " + string);
    }

    @Override
    protected void cancelled() {
        updateProgress(0, numberOfTask);
        updateMessage("cancelled");
        super.cancelled();
    }
    
}
