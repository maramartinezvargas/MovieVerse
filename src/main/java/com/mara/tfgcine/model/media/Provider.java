package com.mara.tfgcine.model.media;

import lombok.Data;

/**
* Representa un proveedor/plataforma de streaming (por ejemplo Netflix, HBO, Prime).
*
* Contiene los datos mínimos necesarios para mostrar un proveedor en la interfaz:
* su identificador (según TMDB), nombre, ruta del logo y enlace a la web del servicio.
*
* Se utiliza en las vistas de detalles de película/serie para listar dónde está disponible el contenido.
*
* @author Tamara Martínez Vargas
*  @since 02/03/2026
* @version 28/05/2026
*/
@Data
public class Provider {
    private String name;
    private String logoPath;
    private String link;
    private int providerId;

    public Provider(int providerId, String name, String logoPath, String link) {
        this.providerId = providerId;
        this.name = name;
        this.logoPath = logoPath;
        this.link = link;
    }

   /* public String getName() { return name; }
    public String getLogoPath() { return logoPath; }
    public String getLink() { return link; }
    public int getProviderId() {return providerId;}*/
}