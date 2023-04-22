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
        // TODO: agregar validacion de ISBN
        this.isbn = isbn;

        // validacion del titulo
        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Titulo no valido!");
        }
        this.titulo = titulo;

        // TODO: Agregar validacion
        this.autor = autor;

        // TODO: Agregar validacion
        this.categoria = categoria;

        this.enUso = false;
        this.calificaciones = new Integer[0];
        this.miembros = new Integer[0];
        System.out.println("AAAA");
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

    public int partnerHasRanked(int idSocio) {
        for (int i = 0; i < miembros.length; i++) {

            int algunaId = miembros[i];

            if (algunaId == idSocio) {
                return i;
            }

        }
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

    public int getFinalCalification() {

        int calificacionFinal;
        int sumaCalificacion = 0;

        if (miembros.length == 0) {
            return -1;
        }

        for (int calificacion : miembros) {
            sumaCalificacion = calificacion + sumaCalificacion;
        }

        if (sumaCalificacion == 0) {
            return -1;
        }

        calificacionFinal = sumaCalificacion / calificaciones.length;
        return calificacionFinal;
    }

    public void removerCalificacion(int idSocio) {

        int index = partnerHasRanked(idSocio);

        if (index == -1) {
            throw new IllegalArgumentException("Lo siento este libro no lo has calificado");
        }

        Utils.removeElement(miembros, miembros[index]);
        Utils.removeElement(calificaciones, calificaciones[index]);

    }

}
