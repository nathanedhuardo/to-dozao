package br.edu.ufape.todozao.exception;

public class InvalidTaskStatusException extends RuntimeException {
    public InvalidTaskStatusException(String status) {
        super("Status de tarefa inválido: " + status);
    }
}

