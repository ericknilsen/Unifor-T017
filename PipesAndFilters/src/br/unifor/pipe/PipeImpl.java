package br.unifor.pipe;

import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PipeImpl<T> implements Pipe<T> {
    private Queue<T> buffer = new LinkedList<T>();
    private boolean isOpenForWriting = true;
    private boolean hasReadLastObject = false;

    @Override
    public synchronized boolean put(T obj) {
        if (!isOpenForWriting) {
            throw new RuntimeException(new IOException("Pipe fechado; impossível escrever nele"));
        } else if (obj == null) {
            throw new IllegalArgumentException("Não é permitido inserir null no pipe; null é usado p/ indicar pipe vazio");
        }

        boolean wasAdded = buffer.add(obj);
        notify();
        
        return wasAdded;
    }

    @Override   
    public synchronized T nextOrNullIfEmptied() throws InterruptedException {
        if (hasReadLastObject) {
            throw new NoSuchElementException("Pipe fechado ou vazio");
        }

        while (buffer.isEmpty()) {
            wait(); 
        }

        T obj = buffer.remove();
        if (obj == null) { 
            hasReadLastObject = true;
        }
        return obj;
    }

    @Override
    public synchronized void closeForWriting() {
        isOpenForWriting = false;
        buffer.add(null);
        notify();
    }
}