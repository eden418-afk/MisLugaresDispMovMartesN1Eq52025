package com.example.mislugares

interface RepositorioLugares {
    fun elemento(id: Int): Lugar          // Devuelve el elemento dado su id
    fun añade(lugar: Lugar)               // Añade el elemento indicado
    fun nuevo(): Int                      // Añade un elemento en blanco y devuelve su id
    fun borrar(id: Int)                   // Elimina el elemento con el id indicado
    fun tamaño(): Int                     // Devuelve el número de elementos
    fun actualiza(id: Int, lugar: Lugar)  // Reemplaza un elemento

    fun añadeEjemplos() {
        añade(
            Lugar(
                nombre = "Escuela Politécnica Superior de Gandía",
                direccion = "C/ Paranimf, 1 46730 Gandia (SPAIN)",
                posicion = GeoPunto(-0.166093, 38.995656),
                tipo = TipoLugar.EDUCACION,
                foto = "",
                telefono = 962849300,
                url = "http://www.epsg.upv.es",
                comentarios = "Uno de los mejores lugares para formarse.",
                valoracion = 3f
            )
        )

        añade(
            Lugar(
                nombre = "Al de siempre",
                direccion = "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
                posicion = GeoPunto(-0.190642, 38.925857),
                tipo = TipoLugar.BAR,
                foto = "",
                telefono = 636472405,
                url = "",
                comentarios = "No te pierdas el arroz en calabaza.",
                valoracion = 3f
            )
        )

        añade(
            Lugar(
                nombre = "androidcurso.com",
                direccion = "ciberespacio",
                posicion = GeoPunto(0.0, 0.0),
                tipo = TipoLugar.EDUCACION,
                foto = "",
                telefono = 962849300,
                url = "http://androidcurso.com",
                comentarios = "Amplía tus conocimientos sobre Android.",
                valoracion = 5f
            )
        )

        añade(
            Lugar(
                nombre = "Barranco del Infierno",
                direccion = "Vía Verde del río Serpis. Villalonga (Valencia)",
                posicion = GeoPunto(-0.295058, 38.867180),
                tipo = TipoLugar.NATURALEZA,
                foto = "",
                telefono = 0,
                url = "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-+verde-del-rio.html",
                comentarios = "Espectacular ruta para bici o andar",
                valoracion = 4f
            )
        )

        añade(
            Lugar(
                nombre = "La Vital",
                direccion = "Avda. de La Vital, 0 46701 Gandía (Valencia)",
                posicion = GeoPunto(-0.1720892, 38.9705949),
                tipo = TipoLugar.COMPRAS,
                foto = "",
                telefono = 962881070,
                url = "http://www.lavital.es/",
                comentarios = "El típico centro comercial",
                valoracion = 2f
            )
        )
    }
}
