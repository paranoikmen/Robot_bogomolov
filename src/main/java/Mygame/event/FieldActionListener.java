package Mygame.event;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface FieldActionListener extends EventListener {
    void robotIsExit(@NotNull FieldActionEvent event);
}
