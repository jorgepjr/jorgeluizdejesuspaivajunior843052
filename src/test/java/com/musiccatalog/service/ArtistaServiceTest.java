package com.musiccatalog.service;

import com.musiccatalog.dto.ArtistaResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.exception.RecordNotFoundException;
import com.musiccatalog.model.Artista;
import com.musiccatalog.repository.ArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArtistaService - Testes Unitários")
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository repository;

    @InjectMocks
    private ArtistaService service;

    private Artista artista;
    private Artista artistaExistente;

    @BeforeEach
    void setup() {
        artista = new Artista("The Beatles", TipoArtista.BANDA);

        artistaExistente = new Artista("The Beatles", TipoArtista.BANDA);
        artistaExistente.setId(1L);
    }

    @Test
    @DisplayName("Deve criar um artista com sucesso")
    void deveCriarArtistaComSucesso() {
        // Arrange
        when(repository.existsByNomeIgnoreCase("The Beatles")).thenReturn(false);
        when(repository.save(any(Artista.class))).thenReturn(artistaExistente);

        // Act
        ArtistaResponse response = service.criar(artista);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1L, response.id(), "ID deveria ser 1");
        assertEquals("The Beatles", response.nome(), "Nome deveria ser The Beatles");
        assertEquals(TipoArtista.BANDA, response.tipo(), "Tipo deveria ser BANDA");
        verify(repository, times(1)).existsByNomeIgnoreCase("The Beatles");
        verify(repository, times(1)).save(any(Artista.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao criar artista com nome duplicado")
    void deveLancarExcecaoAoCriarArtistaComNomeDuplicado() {
        // Arrange
        when(repository.existsByNomeIgnoreCase("The Beatles")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.criar(artista),
                "Deveria lançar RuntimeException para nome duplicado"
        );

        assertEquals("Já existe artista com esse nome.", exception.getMessage());
        verify(repository, times(1)).existsByNomeIgnoreCase("The Beatles");
        verify(repository, never()).save(any(Artista.class));
    }

    @Test
    @DisplayName("Deve editar um artista com sucesso")
    void deveEditarArtistaComSucesso() {
        // Arrange
        Artista artistaAtualizado = new Artista("The Beatles - Updated", TipoArtista.BANDA);
        when(repository.findById(1L)).thenReturn(Optional.of(artistaExistente));
        when(repository.findByNomeIgnoreCase("The Beatles - Updated")).thenReturn(Optional.empty());
        when(repository.save(any(Artista.class))).thenReturn(artistaExistente);

        // Act
        ArtistaResponse response = service.editar(1L, artistaAtualizado);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1L, response.id(), "ID deveria ser 1");
        assertEquals("The Beatles - Updated", response.nome(), "Nome deveria ser atualizado");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Artista.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao editar artista com nome já existente")
    void deveLancarExcecaoAoEditarArtistaComNomeJaExistente() {
        // Arrange
        Artista outroArtista = new Artista("Pink Floyd", TipoArtista.BANDA);
        outroArtista.setId(2L);
        Artista artistaAtualizado = new Artista("Pink Floyd", TipoArtista.BANDA);

        when(repository.findById(1L)).thenReturn(Optional.of(artistaExistente));
        when(repository.findByNomeIgnoreCase("Pink Floyd")).thenReturn(Optional.of(outroArtista));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.editar(1L, artistaAtualizado),
                "Deveria lançar RuntimeException para nome duplicado"
        );

        assertEquals("Já existe artista cadastrado com esse nome", exception.getMessage());
        verify(repository, never()).save(any(Artista.class));
    }

    @Test
    @DisplayName("Deve lançar RecordNotFoundException ao editar artista que não existe")
    void deveLancarExcecaoAoEditarArtistaInexistente() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> service.editar(99L, artista),
                "Deveria lançar RecordNotFoundException para ID inexistente"
        );

        assertNotNull(exception, "Exceção não deveria ser null");
        verify(repository, never()).save(any(Artista.class));
    }

    @Test
    @DisplayName("Deve excluir um artista com sucesso")
    void deveExcluirArtistaComSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(artistaExistente));

        // Act
        service.excluir(1L);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(artistaExistente);
    }

    @Test
    @DisplayName("Deve lançar RecordNotFoundException ao tentar excluir artista que não existe")
    void deveLancarExcecaoAoExcluirArtistaInexistente() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> service.excluir(99L),
                "Deveria lançar RecordNotFoundException para ID inexistente"
        );

        assertNotNull(exception, "Exceção não deveria ser null");
        verify(repository, never()).delete(any(Artista.class));
    }

    @Test
    @DisplayName("Deve retornar um artista quando busca por ID válido")
    void deveRetornarArtistaPorIdValido() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(artistaExistente));

        // Act
        ArtistaResponse response = service.obterPorId(1L);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1L, response.id(), "ID deveria ser 1");
        assertEquals("The Beatles", response.nome(), "Nome deveria ser The Beatles");
        assertEquals(TipoArtista.BANDA, response.tipo(), "Tipo deveria ser BANDA");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar RecordNotFoundException ao buscar artista com ID que não existe")
    void deveLancarExcecaoAoBuscarArtistaPorIdInexistente() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> service.obterPorId(99L),
                "Deveria lançar RecordNotFoundException para ID inexistente"
        );

        assertNotNull(exception, "Exceção não deveria ser null");
        assertNotNull(exception.getMessage(), "Mensagem de erro não deveria ser null");
        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve retornar todos os artistas quando filtro sem parâmetros de pesquisa")
    void deveFiltrarTodosOsArtistasSemParametroDePesquisa() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Artista> artistas = List.of(artistaExistente);
        Page<Artista> page = new PageImpl<>(artistas, pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        Page<ArtistaResponse> response = service.filtrar(null, null, pageable);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1, response.getContent().size(), "Deveria retornar 1 artista");
        assertEquals("The Beatles", response.getContent().get(0).nome(), "Nome deveria ser The Beatles");
        verify(repository, times(1)).findAll(pageable);
        verify(repository, never()).filtrar(anyString(), any(), any());
    }

    @Test
    @DisplayName("Deve retornar artistas filtrados por nome")
    void deveFiltrarArtistasPorNome() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Artista> artistas = List.of(artistaExistente);
        Page<Artista> page = new PageImpl<>(artistas, pageable, 1);
        when(repository.filtrar("The Beatles", null, pageable)).thenReturn(page);

        // Act
        Page<ArtistaResponse> response = service.filtrar("The Beatles", null, pageable);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1, response.getContent().size(), "Deveria retornar 1 artista");
        verify(repository, times(1)).filtrar("The Beatles", null, pageable);
    }

    @Test
    @DisplayName("Deve retornar artistas filtrados por tipo")
    void deveFiltrarArtistasPorTipo() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Artista> artistas = List.of(artistaExistente);
        Page<Artista> page = new PageImpl<>(artistas, pageable, 1);
        when(repository.filtrar(null, TipoArtista.BANDA, pageable)).thenReturn(page);

        // Act
        Page<ArtistaResponse> response = service.filtrar(null, TipoArtista.BANDA, pageable);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertEquals(1, response.getContent().size(), "Deveria retornar 1 artista");
        verify(repository, times(1)).filtrar(null, TipoArtista.BANDA, pageable);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando nenhum artista atende aos critérios de filtro")
    void deveRetornarPaginaVaziaQuandoNenhumArtistaEhEncontrado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Artista> page = new PageImpl<>(List.of(), pageable, 0);
        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        Page<ArtistaResponse> response = service.filtrar(null, null, pageable);

        // Assert
        assertNotNull(response, "Response não deveria ser null");
        assertTrue(response.getContent().isEmpty(), "Lista deveria estar vazia");
        assertEquals(0, response.getTotalElements(), "Total de elementos deveria ser 0");
        verify(repository, times(1)).findAll(pageable);
    }
}