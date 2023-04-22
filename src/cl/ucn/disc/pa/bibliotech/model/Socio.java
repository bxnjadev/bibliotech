/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

import cl.ucn.disc.pa.bibliotech.services.Utils;
import edu.princeton.cs.stdlib.StdOut;

/**
 * Clase que representa a un Socio.
 *
 * @author Programacion Avanzada.
 */
public final class Socio {

    /**
     * Numero maximo de libros que puede tener el Socio.
     */
    private static final int NUMERO_LIBROS_MAXIMO = 5;

    /**
     * Nombre del socio.
     */
    private String nombre;

    /**
     * Apellido del socio.
     */
    private String apellido;

    /**
     * Email del socio.
     */
    private String correo;

    /**
     * Numero del socio.
     */
    private int numeroDeSocio;

    /**
     * Clave del socio.
     */
    private String clave;

    /**
     * Libros que el Socio tiene en prestamo (maximo 10).
     */
    private Libro[] librosEnPrestamo = new Libro[0];

    /**
     * The Constructor.
     *
     * @param nombre        del socio.
     * @param apellido      del socio.
     * @param correo        del socio.
     * @param numeroDeSocio del socio.
     * @param clave         del socio.
     */
    public Socio(String nombre, String apellido, String correo, int numeroDeSocio, String clave) {


        // TODO: agregar validacion
        this.nombre = nombre;

        // TODO: agregar validacion
        this.apellido = apellido;

        // metodo estatico para validacion de email.
        Utils.validarEmail(correo);
        this.correo = correo;

        // TODO: agregar validacion
        this.numeroDeSocio = numeroDeSocio;

        // TODO: agregar validacion
        this.clave = clave;
    }

    /**
     * @return el nombre del Socio.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * @return el apellido del Socio.
     */
    public String getApellido() {
        return this.apellido;
    }

    /**
     * @return el nombre completo del Socio.
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * @return el correo electronico del Socio.
     */
    public String getCorreoElectronico() {
        return this.correo;
    }

    /**
     * @return el numero del Socio.
     */
    public int getNumeroDeSocio() {
        return this.numeroDeSocio;
    }

    /**
     * @return la clave del Socio.
     */
    public String getClave() {
        return this.clave;
    }

    public Libro[] getLibrosEnPrestamo() {
        return librosEnPrestamo;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Setea un nuevo correo para el socio
     *
     * @param correo
     */

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Borra un libro que tiene un socio
     *
     * @param book el libro
     */

    public void borrarLibro(Libro book) {
        librosEnPrestamo = Utils.removeElement(this.librosEnPrestamo, book);

        System.out.println("a: " + librosEnPrestamo.length);
    }

    /**
     * Agrega un libro en prestamo al Socio.
     *
     * @param libro a agregar.
     */
    public void agregarLibro(final Libro libro) {
        // validacion
        if (this.librosEnPrestamo.length == NUMERO_LIBROS_MAXIMO) {
            throw new IllegalArgumentException("El Socio ya tiene la maxima cantidad de libros en prestamo: " + NUMERO_LIBROS_MAXIMO);
        }
        // agrego el libro
        librosEnPrestamo = Utils.append(this.librosEnPrestamo, libro);

        StdOut.println(librosEnPrestamo.length);
    }

    /**
     * Verifica si el socio posee un libro dado un isbn
     *
     * @param isbn el isbn del libro
     * @return devuelve true si tiene el libro y false si no lo tiene
     */

    public boolean tieneLibro(String isbn) {
        boolean tienelibro = false;

        for (Libro libro : librosEnPrestamo) {
            if (libro.getIsbn().equals(isbn)) {
                tienelibro = true;
                break;
            }
        }

        return tienelibro;
    }

}
