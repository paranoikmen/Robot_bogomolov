package robots;

public class IRobot  extends Robot {
    private boolean II = true;

    private PathFinder path;

    public void setPath(PathFinder path) { this.path = path; }

    public void move() {
        move(path.pathFinder());
    }

    @Override
    public boolean getII() { return II; }
}
