package Mygame.event;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface GameActionListener extends EventListener {

    void robotIsMoved(@NotNull GameActionEvent event);

    void robotIsSkipStep(@NotNull GameActionEvent event);

    void robotIsExit(@NotNull GameActionEvent event);
}
