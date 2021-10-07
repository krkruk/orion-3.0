package pl.projektorion.rx.utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.naming.OperationNotSupportedException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ObservableQueue<T> implements BlockingQueue<T>, Closeable {

    private final Subject<T> subject;

    public ObservableQueue() {
        this.subject = PublishSubject.create();
    }

    public Observable<T> observe() {
        return subject;
    }

    @Override
    public void close() throws IOException {
        subject.onComplete();
    }

    @Override
    public boolean add(T t) {
        return this.offer(t);
    }

    @Override
    public boolean offer(T t) {
        this.subject.onNext(t);
        return true;
    }

    @Override
    public void put(T t) throws InterruptedException {
        offer(t);
    }

    @Override
    public boolean offer(T t, long l, TimeUnit timeUnit) throws InterruptedException {
        offer(t);
        return true;
    }

    @Override
    public T take() throws InterruptedException {
        return null;
    }

    @Override
    public T poll(long l, TimeUnit timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit.toMillis(l));
        throw null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        throw new NoSuchElementException();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int drainTo(Collection<? super T> collection) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super T> collection, int i) {
        return 0;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for (var elem : collection) {
            this.offer(elem);
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }
}
