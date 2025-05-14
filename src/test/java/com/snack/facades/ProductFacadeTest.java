package com.snack.facades;

import com.snack.applications.ProductApplication;
import com.snack.entities.Product;
import com.snack.facade.ProductFacade;
import com.snack.repositories.ProductRepository;
import com.snack.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductFacadeTest {
    ProductFacade productFacade;
    Product produtoXBurguer;
    Product produtoXSalada;

    @BeforeEach
    public void setUp() {
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService();
        ProductApplication productApplication = new ProductApplication(productRepository, productService);
        productFacade = new ProductFacade(productApplication);

        produtoXBurguer = new Product(10, "x-burguer", 10, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-burguer.jpg");
        produtoXSalada = new Product(20, "x-salada", 15, "C:\\Users\\aluno.fsa\\MinhasImagens\\x-salada.jpg");
    }

    @Test
    public void testarListaCompletaProdutosChamarMetodoGetAll() {
        // Arrange (fiz no setup())

        // Act
        productFacade.append(produtoXBurguer);
        productFacade.append(produtoXSalada);
        List<Product> produtos = productFacade.getAll();

        // Assert
        Assertions.assertEquals(2, produtos.size());
    }

    @Test
    public void retornarProdutoCorretoAoBuscarPorIdValido() {
        productFacade.append(produtoXBurguer);
        Product result = productFacade.getById(produtoXBurguer.getId());
        Assertions.assertEquals("x-burguer", result.getDescription());
    }

    @Test
    public void testarMetodoExistsParaIdValidoERetornarTrue() {
        productFacade.append(produtoXBurguer);
        Assertions.assertTrue(productFacade.exists(produtoXBurguer.getId()));
    }

    @Test
    public void testarMetodoExistsParaIdInvalidoERetornarFalse() {
        Assertions.assertFalse(productFacade.exists(123));
    }

    @Test
    public void adicionarNovoProdutoComMetodoAppendDeveFuncionar() {
        productFacade.append(produtoXSalada);
        Assertions.assertEquals(1, productFacade.getAll().size());
    }

    @Test
    public void removerProdutoExistenteComMetodoRemove() {
        productFacade.append(produtoXSalada);
        productFacade.remove(produtoXSalada.getId());
        Assertions.assertFalse(productFacade.exists(produtoXSalada.getId()));
    }

}
