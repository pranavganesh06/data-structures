import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;

public class BruinArrayList<E> implements List<E> {

    @SuppressWarnings("unchecked")
    private E[] data = (E[]) new Object[10];

    private int size;

    public BruinArrayList() {
        this(10);
    }

    @SuppressWarnings("unchecked")
    public BruinArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

        data = (E[]) new Object[capacity];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (size == data.length) {
            data = java.util.Arrays.copyOf(data, data.length * 2);
        }

        data[size] = e;
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        if (size == data.length) {
            data = java.util.Arrays.copyOf(data, data.length * 2);
        }

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = element;
        size++;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return data[index];
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], o)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        E removed = data[index];

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
        data[size] = null;

        return removed;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);

        if (index == -1) {
            return false;
        }

        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        E old = data[index];
        data[index] = element;

        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size() - 1; i >= 0; i--) {
            if (Objects.equals(get(i), o)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);

        boolean changed = false;

        for (E element : c) {
            add(element);
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Objects.requireNonNull(c);

        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        int originalSize = size();
        int offset = 0;

        for (E element : c) {
            add(index + offset, element);
            offset++;
        }

        return size() != originalSize;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);

        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);

        boolean changed = false;

        for (int i = size() - 1; i >= 0; i--) {
            if (!c.contains(get(i))) {
                remove(i);
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);

        boolean changed = false;

        for (int i = size() - 1; i >= 0; i--) {
            if (c.contains(get(i))) {
                remove(i);
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new BruinArrayListIterator<E>(this);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new BruinArrayListIterator<E>(this);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new BruinArrayListIterator<E>(this, index);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];

        for (int i = 0; i < size(); i++) {
            result[i] = get(i);
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Objects.requireNonNull(a);

        if (a.length < size()) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),
                    size());
        }

        for (int i = 0; i < size(); i++) {
            a[i] = (T) get(i);
        }

        if (a.length > size()) {
            a[size()] = null;
        }

        return a;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof List<?>)) {
            return false;
        }

        List<?> otherList = (List<?>) other;

        if (otherList.size() != size()) {
            return false;
        }

        Iterator<?> thisIterator = iterator();
        Iterator<?> otherIterator = otherList.iterator();

        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            if (!Objects.equals(thisIterator.next(), otherIterator.next())) {
                return false;
            }
        }

        return !thisIterator.hasNext() && !otherIterator.hasNext();
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (E element : this) {
            result = 31 * result + Objects.hashCode(element);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {
            result.append(String.valueOf(iterator.next()));

            if (iterator.hasNext()) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

    @Override
    public Spliterator<E> spliterator() {
        return List.super.spliterator();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList is not required");
    }
}