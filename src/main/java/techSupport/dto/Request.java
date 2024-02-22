package techSupport.dto;

import java.time.Instant;

public class Request {
    int requestId;
    int userId;
    String number;
    String description;
    Instant createDate;
    Status status;
    String comment;
    int score;
    String info;

    // Геттеры
    public int getRequestId() {
        return requestId;
    }

    public int getUserId() {
        return userId;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Status getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public int getScore() {
        return score;
    }

    public String getInfo() {
        return info;
    }

    // Сеттеры
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
