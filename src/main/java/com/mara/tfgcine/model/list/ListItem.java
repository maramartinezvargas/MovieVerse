package com.mara.tfgcine.model.list;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un elemento dentro de una lista de contenido multimedia.
 *
 * Almacena el identificador del medio, la fecha en la que fue añadido y la relación
 * con la lista a la que pertenece.<
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@Entity
@Table(name = "list_items")
public class ListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private MediaList list;

    public ListItem() {}
}