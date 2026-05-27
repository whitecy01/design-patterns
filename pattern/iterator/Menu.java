import composite.MenuItem;
import java.util.Iterator;

public interface Menu {
    Iterator<MenuItem> createIterator();
}