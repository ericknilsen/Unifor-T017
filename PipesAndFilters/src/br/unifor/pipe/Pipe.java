package br.unifor.pipe;

public interface Pipe<T> {
    public boolean put(T obj);
    public T nextOrNullIfEmptied() throws InterruptedException;
    public void closeForWriting();
}