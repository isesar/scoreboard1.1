package enums;

public enum Team {
    MEXICO("Mexico"),
    CANADA("Canada"),
    SPAIN("Spain"),
    BRAZIL("Brazil"),
    GERMANY("Germany"),
    FRANCE("France"),
    URUGUAY("Uruguay"),
    ITALY("Italy"),
    ARGENTINA("Argentina"),
    AUSTRALIA("Australia");

    private final String displayName;

    Team(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}