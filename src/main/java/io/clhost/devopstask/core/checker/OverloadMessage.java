package io.clhost.devopstask.core.checker;

public class OverloadMessage {
    private String text;
    private OverloadType type;

    public OverloadMessage(String text, OverloadType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public OverloadType getType() {
        return type;
    }

    enum OverloadType {
        RAM,
        CPU,
        DISK
    }

    @Override
    public String toString() {
        return text;
    }
}
