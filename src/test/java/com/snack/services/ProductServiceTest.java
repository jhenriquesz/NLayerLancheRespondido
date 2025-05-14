package com.snack.services;

import com.snack.entities.Product;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

public class ProductServiceTest {
    ProductService productService;
    Product produtoXBurguer;
    Product produtoXSalada;

    Path imagemXBurguerPath;
    Path imagemXSaladaPath;

    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("C:\\Users\\aluno.fsa\\MinhasImagens"));

        imagemXBurguerPath = Paths.get("C:\\Users\\aluno.fsa\\MinhasImagens\\x-burguer.jpg");
        imagemXSaladaPath = Paths.get("C:\\Users\\aluno.fsa\\MinhasImagens\\x-salada.jpg");

        // Cria arquivos de imagem falsos
        Files.write(imagemXBurguerPath, "fake".getBytes());
        Files.write(imagemXSaladaPath, "fake".getBytes());

        produtoXBurguer = new Product(50, "x-burguer", 10, imagemXBurguerPath.toString());
        produtoXSalada = new Product(50, "x-salada", 15, imagemXSaladaPath.toString());

        productService = new ProductService();
    }

    @AfterEach
    public void limparArquivosDeTeste() throws IOException {
        Files.deleteIfExists(imagemXBurguerPath);
        Files.deleteIfExists(imagemXSaladaPath);
        Files.deleteIfExists(Paths.get("C:\\Users\\aluno.fsa\\MeuSistemaLanche\\50.jpg"));
    }

    @Test
    public void atualizarUmProdutoExistente() {
        productService.save(produtoXBurguer);
        productService.update(produtoXSalada);

        Assertions.assertEquals("x-salada", produtoXSalada.getDescription());
        Assertions.assertEquals(15, produtoXSalada.getPrice());

        Path path = Paths.get(produtoXSalada.getImage());
        Assertions.assertTrue(Files.exists(path));
    }

    @Test
    public void testarSalvarProdutoComImagemValida() {
        boolean resultado = productService.save(produtoXBurguer);
        Assertions.assertTrue(resultado);
    }

    @Test
    public void testarSalvarProdutoComImagemInexistente() {
        Product produtoInvalido = new Product(60, "x-bacon", 12, "C:\\caminho\\inexistente.jpg");
        boolean resultado = productService.save(produtoInvalido);
        Assertions.assertFalse(resultado);
    }

    @Test
    public void testarRemoverProdutoExistenteComSucesso() {
        productService.save(produtoXBurguer);
        productService.remove(produtoXBurguer.getId());

        Path path = Paths.get("C:\\Users\\aluno.fsa\\MeuSistemaLanche\\50.jpg");
        Assertions.assertFalse(Files.exists(path));
    }

    @Test
    public void validarCaminhoDaImagemRecuperadoPorId() {
        productService.save(produtoXBurguer);
        String caminho = productService.getImagePathById(produtoXBurguer.getId());
        Assertions.assertNotNull(caminho);
        Assertions.assertTrue(caminho.endsWith("50.jpg"));
    }
}
