package me.starchaser;

import java.util.ArrayList;

public class KPEvents {
    private final String event_name;
    ArrayList<String> commands;
    public KPEvents(String event_name){
        this.event_name = event_name;
    }
    public void addCommand(String command) {
        commands.add(command);
    }
    public void removeCommand(String command) {
        commands.remove(command);
    }
    public ArrayList<String> getCommands() {
        return commands;
    }

    public String getEventName() {
        return event_name;
    }
}
