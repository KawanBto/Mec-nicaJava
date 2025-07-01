package br.com.fatec.DAO;

import java.util.List;

public interface DAO<T> { 

    boolean cadastrar(T obj);
    boolean alterar(T obj);
    boolean excluir(int id); 
    T buscarPorId(int id); 
    List<T> buscarTodos();

    List<T> listar(String criterio);
}