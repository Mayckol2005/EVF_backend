package com.backend.tienda.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrdenRequest {
    // Solo necesitamos esto del frontend.
    // El resto (precios, totales, usuario) lo calculamos en el backend por seguridad.
    private List<ItemRequest> items;
}