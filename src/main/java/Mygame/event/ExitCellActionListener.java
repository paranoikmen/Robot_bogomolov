package Mygame.event;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ExitCellActionListener extends EventListener {
    void robotIsExit(@NotNull ExitCellActionEvent event);
}
