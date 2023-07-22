package sg.edu.np.mad.team5MADAssignmentOnTask;

public class UserCount {
    private int coincount;
    private int taskcount;
    private int completedtaskcount;
    private int logincount;
    private int achievementcomplete;

    public UserCount(int coincount, int taskcount, int completedtaskcount, int logincount, int achievementcomplete) {
        this.coincount = coincount;
        this.taskcount = taskcount;
        this.completedtaskcount = completedtaskcount;
        this.logincount = logincount;
        this.achievementcomplete = achievementcomplete;
    }

    public int getCoincount() {
        return coincount;
    }

    public void setCoincount(int coincount) {
        this.coincount = coincount;
    }

    public int getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(int taskcount) {
        this.taskcount = taskcount;
    }

    public int getCompletedtaskcount() {
        return completedtaskcount;
    }

    public void setCompletedtaskcount(int completedtaskcount) {
        this.completedtaskcount = completedtaskcount;
    }

    public int getLogincount() {
        return logincount;
    }

    public void setLogincount(int logincount) {
        this.logincount = logincount;
    }

    public int getAchievementcomplete() {
        return achievementcomplete;
    }

    public void setAchievementcomplete(int achievementcomplete) {
        this.achievementcomplete = achievementcomplete;
    }
}


