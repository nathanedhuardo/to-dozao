package br.edu.ufape.todozao.exception;

public class TaskDuplicateException extends RuntimeException {
    public TaskDuplicateException(String message) {
        super(message);
    }
}

