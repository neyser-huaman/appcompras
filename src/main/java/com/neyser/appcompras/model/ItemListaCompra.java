package com.neyser.appcompras.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemListaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    private BigDecimal peso;

    private String observacion;

    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "lista_compra_id")
    private ListaCompra listaCompra;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}