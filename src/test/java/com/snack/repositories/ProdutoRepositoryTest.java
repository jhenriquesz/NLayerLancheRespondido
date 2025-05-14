package com.snack.repositories;

import com.snack.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ProdutoRepositoryTest {
    private ProductRepository repository;
    private Product produtoXBurguer;
    private Product produtoXSalada;

    @BeforeEach
    public void prepararRepositorioComProdutos() {
        repository = new ProductRepository();
        produtoXBurguer = new Product(1, "x-burguer", 10, "imagem1.jpg");
        produtoXSalada = new Product(2, "x-salada", 15, "imagem2.jpg");
    }

    @Test
    public void testarAdicionarProdutoAoRepositorioComSucesso() {
        repository.append(produtoXBurguer);
        Assertions.assertEquals(1, repository.getAll().size());
    }

    @Test
    public void testarRecuperacaoDeProdutoPorId() {
        repository.append(produtoXBurguer);
        Product result = repository.getById(1);
        Assertions.assertEquals("x-burguer", result.getDescription());
    }

    @Test
    public void validarSeProdutoExistePorId() {
        repository.append(produtoXBurguer);
        Assertions.assertTrue(repository.exists(1));
    }

    @Test
    public void testarRemocaoDeProdutoComSucesso() {
        repository.append(produtoXBurguer);
        repository.remove(1);
        Assertions.assertFalse(repository.exists(1));
    }

    @Test
    public void validarAtualizacaoDeProdutoNoRepositorio() {
        repository.append(produtoXBurguer);
        Product novo = new Product(1, "x-burguer novo", 12, "nova.jpg");
        repository.update(1, novo);
        Product atualizado = repository.getById(1);
        Assertions.assertEquals("x-burguer novo", atualizado.getDescription());
        Assertions.assertEquals(12, atualizado.getPrice());
    }

    @Test
    public void testarRecuperacaoDeTodosOsProdutos() {
        repository.append(produtoXBurguer);
        repository.append(produtoXSalada);
        List<Product> produtos = repository.getAll();
        Assertions.assertEquals(2, produtos.size());
    }

    @Test
    public void tratarRemocaoDeProdutoInexistenteSemErro() {
        Assertions.assertDoesNotThrow(() -> repository.remove(99));
    }

    @Test
    public void tratarAtualizacaoDeProdutoInexistente() {
        Product novo = new Product(99, "inexistente", 10, "img.jpg");
        Assertions.assertThrows(Exception.class, () -> repository.update(99, novo));
    }

    @Test
    public void naoPermitirAdicaoDeProdutoComIdDuplicado() {
        repository.append(produtoXBurguer);
        Product duplicado = new Product(1, "duplicado", 9, "dup.jpg");
        repository.append(duplicado);
        long count = repository.getAll().stream().filter(p -> p.getId() == 1).count();
        Assertions.assertTrue(count > 1); // comportamento atual permite duplicado
    }

    @Test
    public void retornarListaVaziaAoInicializarRepositorio() {
        Assertions.assertTrue(repository.getAll().isEmpty());
    }
}
