package com.backend.tienda.dto;

import lombok.Data;

@Data
public class ItemRequest {
    private Long productoId;
    private Integer cantidad;
}