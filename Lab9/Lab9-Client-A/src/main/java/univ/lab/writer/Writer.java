package univ.lab.writer;

public interface Writer<T> {
    void write(String filename, T instance);
}
