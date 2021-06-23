package ua.startit;

public class Props {
    private static Props ourInstance = new Props();

    public static Props getInstance() {
        return ourInstance;
    }

    private Props() {
    }
}