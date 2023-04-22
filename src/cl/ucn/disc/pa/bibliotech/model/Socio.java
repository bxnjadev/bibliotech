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
    private String mail;

    /**
     * Numero del socio.
     */
    private int numeroDeSocio;

    /**
     * Contrasenia del socio.
     */
    private String password;

    /**
     * Libros que el Socio tiene en prestamo (maximo 10).
     */
    private Libro[] librosEnPrestamo = new Libro[0];

    /**
     * The Constructor.
     *
     * @param nombre            del socio.
     * @param apellido          del socio.
     * @param correoElectronico del socio.
     * @param numeroDeSocio     del socio.
     * @param contrasenia       del socio.
     */
    public Socio(String nombre, String apellido, String mail, int numeroDeSocio, String contrasenia) {


        // TODO: agregar validacion
        this.nombre = nombre;

        // TODO: agregar validacion
        this.apellido = apellido;

        // metodo estatico para validacion de email.
        Utils.validarEmail(mail);
        this.mail = mail;

        // TODO: agregar validacion
        this.numeroDeSocio = numeroDeSocio;

        // TODO: agregar validacion
        this.password = contrasenia;
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
        return this.mail;
    }

    /**
     * @return el numero del Socio.
     */
    public int getNumeroDeSocio() {
        return this.numeroDeSocio;
    }

    /**
     * @return la contrasenia del Socio.
     */
    public String getPassword() {
        return this.password;
    }

    public Libro[] getLibrosEnPrestamo() {
        return librosEnPrestamo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void deleteBook(Libro book) {
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

    public boolean hasBook(String isbn) {
        boolean hasBook = false;

        for (Libro libro : librosEnPrestamo) {
            if (libro.getIsbn().equals(isbn)) {
                hasBook = true;
                break;
            }
        }

        return hasBook;
    }

}
