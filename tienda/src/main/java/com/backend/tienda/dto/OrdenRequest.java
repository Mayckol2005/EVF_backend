package com.backend.tienda.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrdenRequest {

    private List<ItemRequest> items;
}