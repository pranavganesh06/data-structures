import java.util.ListIterator;
import java.util.NoSuchElementException;

public class BruinArrayListIterator<E> implements ListIterator<E> {

    private final BruinArrayList<E> list;
    private int cursor;
    private int lastReturned = -1;

    BruinArrayListIterator(BruinArrayList<E> list) {
        this(list, 0);
    }

    BruinArrayListIterator(BruinArrayList<E> list, int index) {
        this.list = list;
        this.cursor = index;
    }

    @Override
    public boolean hasNext() {
        return cursor < list.size();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return list.get(lastReturned = cursor++);
    }

    @Override
    public boolean hasPrevious() {
        return cursor > 0;
    }

    @Override
    public E previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }

        return list.get(lastReturned = --cursor);
    }

    @Override
    public int nextIndex() {
        return cursor;
    }

    @Override
    public int previousIndex() {
        return cursor - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Iterator removal is not supported");
    }

    @Override
    public void set(E e) {
        if (lastReturned < 0) {
            throw new IllegalStateException("next() or previous() not called");
        }

        list.set(lastReturned, e);
    }

    @Override
    public void add(E e) {
        list.add(cursor, e);
        cursor++;
        lastReturned = -1;
    }
}