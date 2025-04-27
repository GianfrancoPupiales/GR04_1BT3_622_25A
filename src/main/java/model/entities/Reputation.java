package model.entities;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Reputation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idReputation;

    private int totalScore;

    private int totalVotes;

    private double averageScore;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    public Reputation() {}

    public Reputation(User user) {
        this.user = user;
        this.totalScore = 0;
        this.totalVotes = 0;
        this.averageScore = 0.0;
    }

    public void addRating(int score) {
        this.totalScore += score;
        this.totalVotes++;
        this.averageScore = (double) totalScore / totalVotes;
    }

    // Getters y setters

    public int getIdReputation() {
        return idReputation;
    }

    public void setIdReputation(int idReputation) {
        this.idReputation = idReputation;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return idReputation;
    }
}
