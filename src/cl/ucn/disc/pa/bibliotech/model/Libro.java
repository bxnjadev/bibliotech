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

    private Integer[] greats = new Integer[0];

    private Integer[] members = new Integer[0];

    private boolean inUse;

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

        this.inUse = false;
        this.greats = new Integer[0];
        this.members = new Integer[0];
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

    public void rankBook(int calification, int partnerId) {

        if (greats == null) {
            System.out.println("Es null greats");
        }

        if (members == null) {
            System.out.println("Es null members");
        }

        greats = Utils.append(greats, calification);
        members = Utils.append(members, partnerId);
    }

    public boolean inUse() {
        return inUse;
    }

    public void updateAsUsed() {
        this.inUse = true;
    }

    public void updateAsNotUsed() {
        this.inUse = false;
    }

    public Integer[] getGreats() {
        return this.greats;
    }

    public Integer[] getMembers() {
        return this.members;
    }

    public int getFinalCalification() {

        int finalCalification;
        int sumCalification = 0;

        if (greats.length == 0) {
            return -1;
        }

        for (int calification : greats) {
            sumCalification = calification + sumCalification;
        }

        if (sumCalification == 0) {
            return -1;
        }

        finalCalification = sumCalification / greats.length;
        return finalCalification;
    }

    public void unRankBook(int partnerId) {

    }

}
