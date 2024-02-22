package techSupport.dto;

public enum Role {
    CLIENT,
    SUPPORT_STAFF,
    PERFORMER,
    ADMINISTRATOR;

    public static Role fromString(String roleStr) {
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
