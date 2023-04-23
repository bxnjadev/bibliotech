/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

import cl.ucn.disc.pa.bibliotech.services.Utils;

/**
 * Clase que representa un Libro.
 *
 * @author Programacion Avanzada.
 */
public final class Libro {

    /**
     * The ISBN.
     */
    private String isbn;

    /**
     * The Titulo.
     */
    private String titulo;

    /**
     * The Author.
     */
    private String autor;

    /**
     * The Categoria
     */
    private String categoria;

    private Integer[] calificaciones = new Integer[0];

    private Integer[] miembros = new Integer[0];

    private boolean enUso;

    /**
     * The Constructor.
     *
     * @param isbn      del libro.
     * @param titulo    del libro.
     * @param autor     del libro
     * @param categoria del libro.
     */
    public Libro(final String isbn, final String titulo, final String autor, final String categoria) {
        Utils.validarString(isbn, "El isbn no puede ser nulo");
        this.isbn = isbn;

        // validacion del titulo
        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Titulo no valido!");
        }
        this.titulo = titulo;

        Utils.validarString(autor, "El autor no puede ser nulo");
        this.autor = autor;

        Utils.validarString(categoria, "La categoria no puede ser nulo");
        this.categoria = categoria;

        this.enUso = false;
        this.calificaciones = new Integer[0];
        this.miembros = new Integer[0];
    }

    /**
     * @return the ISBN.
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * @return the titulo.
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * @return the autor.
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @return the categoria.
     */
    public String getCategoria() {
        return this.categoria;
    }

    /**
     * Califica el libro
     *
     * @param calificacion la calificacion que es del 0 al 5
     * @param idSocio      la id del socio que lo califico
     */

    public void calificarLibro(int calificacion, int idSocio) {
        calificaciones = Utils.append(calificaciones, calificacion);
        miembros = Utils.append(miembros, idSocio);
    }

    public int socioHaCalificadoEsteLibro(int idSocio) {

        //Recorre todos los socios

        for (int i = 0; i < miembros.length; i++) {

            int algunaId = miembros[i];

            //Verifica si la id es igual a la del socio y devuelve esa posición

            if (algunaId == idSocio) {
                return i;
            }

        }

        //Si no lo encuentra devuelva -1
        return -1;
    }

    public boolean enUso() {
        return enUso;
    }

    public void marcarComoEnUso() {
        this.enUso = true;
    }

    public void sacarDeUso() {
        this.enUso = false;
    }

    public Integer[] getCalificaciones() {
        return this.calificaciones;
    }

    public Integer[] getMiembros() {
        return this.miembros;
    }

    public int getCalificacionFinal() {

        int calificacionFinal;
        int sumaCalificacion = 0;

        //Verifica si el libro tiene calificaciones

        if (calificaciones.length == 0) {
            return -1;
        }

        //Recorre todas las calificaciones y las suma

        for (Integer calificacion : calificaciones) {

            if (calificacion == null) {
                continue;
            }

            sumaCalificacion = calificacion + sumaCalificacion;
        }

        //Si no sumo ninguna devuelve -1
        if (sumaCalificacion == 0) {
            return -1;
        }

        //Obtiene el promedio y se imprima
        calificacionFinal = sumaCalificacion / calificaciones.length;
        return calificacionFinal;
    }

    public void removerCalificacion(int idSocio) {

        //Se obitene la posicion
        int index = socioHaCalificadoEsteLibro(idSocio);

        //Si es -1 se lanzar error
        if (index == -1) {
            throw new IllegalArgumentException("Lo siento este libro no lo has calificado");
        }

        //Se elimina de calificaciones
        calificaciones = Utils.removeElement(miembros, miembros[index]);
        miembros = Utils.removeElement(calificaciones, calificaciones[index]);

    }

}
