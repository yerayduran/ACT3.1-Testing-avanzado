import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class VideojuegoServiceTest {

    private VideojuegoRepository repositoryMock;
    private VideojuegoService service;

    @BeforeEach
    void setUp() {
        repositoryMock = Mockito.mock(VideojuegoRepository.class);
        service = new VideojuegoService(repositoryMock);
    }

    @Test
    void clasificarJuego_limites() {
        assertEquals("Malo", service.clasificarJuego(0));
        assertEquals("Malo", service.clasificarJuego(49));
        assertEquals("Bueno", service.clasificarJuego(50));
        assertEquals("Bueno", service.clasificarJuego(89));
        assertEquals("Obra Maestra", service.clasificarJuego(90));
    }

    @Test
    void clasificarJuego_fueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> service.clasificarJuego(120));
    }

    @ParameterizedTest
    @CsvSource({
            "PC, 60, true",
            "PC, 40, false",
            "Movil, 25, true",
            "Movil, 20, false"
    })
    void esJuegoLargo_parametrizado(String plataforma, int horas, boolean esperado) {
        Videojuego juego = new Videojuego("Test", plataforma, horas, 80);
        assertEquals(esperado, service.esJuegoLargo(juego));
    }

    @Test
    void registrarJuego_tituloNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego(null, "PC", 10, 80)
        );
    }

    @Test
    void registrarJuego_conTituloYHorasNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego("Sonic", "PC", -20, 80)
        );
    }

    @Test
    void registrarJuego_tituloVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarJuego("", "PC", 10, 80)
        );
    }

    @Test
    void registrarJuego_valido_verifyGuardar() {
        service.registrarJuego("Zelda", "Switch", 80, 95);
        verify(repositoryMock, times(1)).guardar(any(Videojuego.class));
    }

    @Test
    void stub_buscarEldenRing() {
        Videojuego eldenRing = new Videojuego("Elden Ring", "PC", 120, 95);
        when(repositoryMock.buscarPorTitulo("Elden Ring")).thenReturn(eldenRing);

        Videojuego resultado = repositoryMock.buscarPorTitulo("Elden Ring");

        assertNotNull(resultado);
        assertEquals("Elden Ring", resultado.getTitulo());
    }
}