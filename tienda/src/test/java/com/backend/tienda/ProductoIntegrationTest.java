package com.backend.tienda;

import com.backend.tienda.model.Producto;
import com.backend.tienda.repository.DetalleOrdenRepository; // <--- Importamos el repositorio de detalles
import com.backend.tienda.repository.OrdenRepository;
import com.backend.tienda.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository; // <--- Inyectamos el repositorio

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // --- ESTRATEGIA DE LIMPIEZA SEGURA ---
        // El orden es crítico para evitar errores de Clave Foránea (FK Constraints)

        // 1. Borrar la tabla hija/intermedia (Detalles)
        // Esto libera las referencias tanto a Órdenes como a Productos
        detalleOrdenRepository.deleteAll();

        // 2. Borrar las Órdenes (padres de los detalles)
        ordenRepository.deleteAll();

        // 3. Borrar los Productos (ahora sí es seguro, nadie los referencia)
        productoRepository.deleteAll();

        // --- CREACIÓN DE DATOS DE PRUEBA ---
        Producto p = new Producto();
        p.setName("Perfume Test");
        p.setBrand("Marca Test");
        p.setPrice(5000);
        p.setStock(100);
        p.setNormalPrice(6000);
        // Asegúrate de llenar campos obligatorios si los hay en tu entidad
        productoRepository.save(p);
    }

    @Test
    void obtenerProductos_DebeRetornarLista() throws Exception {
        // Prueba GET público
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Perfume Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simulamos usuario Admin
    void crearProducto_ComoAdmin_DebeFuncionar() throws Exception {
        // Prueba POST autenticado
        Producto nuevo = new Producto();
        nuevo.setName("Nuevo Perfume");
        nuevo.setBrand("Nueva Marca");
        nuevo.setPrice(10000);
        nuevo.setStock(50);
        nuevo.setNormalPrice(12000);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nuevo Perfume"));
    }

    @Test
    void crearProducto_SinAuth_DebeFallar() throws Exception {
        // Prueba de seguridad: Intentar crear sin estar logueado
        Producto nuevo = new Producto();
        nuevo.setName("Intruso");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden()); // Esperamos 403 Forbidden
    }
}