package com.snack.applications;

import com.snack.entities.Product;
import com.snack.repositories.ProductRepository;
import com.snack.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ProductApplicationTest {
    ProductApplication productApplication;
    Product produtoXBurguer;
    Product produtoXSalada;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService();

        produtoXBurguer = new Product(1, "x-burguer", 10, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-burguer.jpg");
        produtoXSalada = new Product(2, "x-salada", 15, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-salada.jpg");

        productApplication = new ProductApplication(productRepository, productService);
    }

    @Test
    public void tentarRemoverProdutoIdInexistente() {
        // Arrange (Fizemos no setUp())
        // Act
        productApplication.append(produtoXBurguer);
        productApplication.append(produtoXSalada);

        // Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            productApplication.remove(3);
        });
    }

    @Test
    public void tentarRemoverProdutoExistente() {
        // Arrange (Fizemos no setUp())
        Product produtoXBurguerZ = new Product(100, "x-burguer", 10, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-burguer.jpg");
        Product produtoXSaladaZ = new Product(100, "x-salada", 15, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-salada.jpg");

        // Act
        productApplication.append(produtoXBurguerZ);
        productApplication.append(produtoXSaladaZ);
        productApplication.remove(100);

        Path path = Paths.get(produtoXBurguerZ.getImage());
        Assertions.assertFalse(Files.exists(path));
    }

    @Test
    public void testarListagemDeTodosOsProdutosDoRepositorio() {
        productApplication.append(produtoXBurguer);
        productApplication.append(produtoXSalada);
        Assertions.assertEquals(2, productApplication.getAll().size());
    }

    @Test
    public void obterProdutoPorIdValidoDeveRetornarProdutoEsperado() {
        productApplication.append(produtoXBurguer);
        Product result = productApplication.getById(produtoXBurguer.getId());
        Assertions.assertEquals("x-burguer", result.getDescription());
    }

    @Test
    public void obterProdutoPorIdInvalidoDeveLancarErro() {
        Assertions.assertThrows(Exception.class, () -> {
            productApplication.getById(999);
        });
    }

    @Test
    public void verificarExistenciaProdutoPorIdValidoDeveRetornarTrue() {
        productApplication.append(produtoXSalada);
        Assertions.assertTrue(productApplication.exists(produtoXSalada.getId()));
    }

    @Test
    public void verificarExistenciaProdutoPorIdInvalidoDeveRetornarFalse() {
        Assertions.assertFalse(productApplication.exists(999));
    }

    @Test
    public void adicionarProdutoComImagemValidaDeveSalvarComSucesso() {
        productApplication.append(produtoXBurguer);
        Path imagem = Paths.get("C:\\Users\\aluno.fsa\\MeuSistemaLanche\\1.jpg");
        Assertions.assertTrue(Files.exists(imagem));
    }

    @Test
    public void atualizarProdutoExistenteDeveSubstituirImagem() {
        productApplication.append(produtoXBurguer);
        Product atualizado = new Product(1, "x-burguer atualizado", 20, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-salada.jpg");
        productApplication.update(1, atualizado);
        Product result = productApplication.getById(1);
        Assertions.assertEquals("x-burguer atualizado", result.getDescription());
        Assertions.assertEquals(20, result.getPrice());
    }

}