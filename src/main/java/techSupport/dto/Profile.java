package techSupport.dto;

public enum Profile {
    PROGRAMMER,
    MANAGER,
    DATABASE_SPECIALIST,
    SYSTEM_ADMINISTRATOR;

    public static Profile fromString(String profileStr) {
        try {
            return Profile.valueOf(profileStr.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static String toText(Profile profile) {
        if (profile == Profile.PROGRAMMER) {
            return "программист";
        } else if (profile == Profile.MANAGER) {
            return "менеджер";
        } else if (profile == Profile.DATABASE_SPECIALIST) {
            return "специалист по БД";
        } else if (profile == Profile.SYSTEM_ADMINISTRATOR) {
            return "сисадмин";
        }
        return "";
    }
}

