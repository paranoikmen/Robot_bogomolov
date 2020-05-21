package robots.event;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface RobotActionListener extends EventListener {
    void robotIsMoved(@NotNull RobotActionEvent event);

    void robotSkippedStep(@NotNull RobotActionEvent event);

    void robotActivityChanged(@NotNull RobotActionEvent event);

}
