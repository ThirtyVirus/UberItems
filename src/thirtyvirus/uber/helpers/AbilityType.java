package thirtyvirus.uber.helpers;

public enum AbilityType {
    LEFT_CLICK("LEFT CLICK"),
    RIGHT_CLICK("RIGHT CLICK"),
    MIDDLE_CLICK("MIDDLE CLICK"),
    FULL_SET_BONUS("FULL SET BONUS"),
    NONE("");

    private String text;

    AbilityType(String text) { this.text = text; }
    public String getText() { return text; }
}
