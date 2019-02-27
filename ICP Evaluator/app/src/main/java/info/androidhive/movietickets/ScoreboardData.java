package info.androidhive.movietickets;

public class ScoreboardData {
    String teamname;
    float average;

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "ScoreboardData{" +
                "teamname='" + teamname + '\'' +
                ", average=" + average +
                '}';
    }
}
