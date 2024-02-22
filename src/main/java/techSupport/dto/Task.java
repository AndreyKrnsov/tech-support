package techSupport.dto;

import java.time.Instant;

public class Task {
    int taskId;
    String description;
    int requestId;
    int clientId;
    int staffId;
    int performerId;
    Instant createDate;
    Status status;
    String comment;
    String info;

    // Геттеры
    public int getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getPerformerId() {
        return performerId;
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

    public String getInfo() {
        return info;
    }

    // Сеттеры
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public void setPerformerId(int performerId) {
        this.performerId = performerId;
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

    public void setInfo(String info) {
        this.info = info;
    }
}
