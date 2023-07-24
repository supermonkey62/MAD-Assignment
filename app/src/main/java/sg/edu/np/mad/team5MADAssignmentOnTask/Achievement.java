package sg.edu.np.mad.team5MADAssignmentOnTask;
public class Achievement {
    private String title;
    private int progress;
    private int maxProgress;
    private String reward;
    private String status;
    private String rewardtype;

    public Achievement() {
        // Empty constructor required for Firebase
    }

    public Achievement(String title, int progress, int maxProgress,String reward,String rewardtype,String status) {
        this.title = title;
        this.progress = progress;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.status = status;
        this.rewardtype = rewardtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRewardtype() {
        return rewardtype;
    }

    public void setRewardtype(String rewardtype) {
        this.rewardtype = rewardtype;
    }


}

