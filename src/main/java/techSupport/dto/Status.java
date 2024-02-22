package techSupport.dto;

public enum Status {
    OPEN,
    IN_PROGRESS,
    COMPLETE;

    public static Status fromString(String statusStr) {
        try {
            return Status.valueOf(statusStr.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
