package br.edu.ufape.todozao.exception;

public class TaskNotFoundException extends RuntimeException {
    public  TaskNotFoundException(Long id){
        super("Task não encontrada: " + id);
    }
}
