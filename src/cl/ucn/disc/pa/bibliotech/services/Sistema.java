/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.princeton.cs.stdlib.StdOut;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * The Sistema.
     */
    public Sistema() throws IOException {

        // no hay socio logeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        // carga de los socios y libros.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            // no se encontraron datos, se agregar los por defecto.

            System.out.println("test");

            // creo un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            // creo un libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", " Kathy Sierra", "Programming Languages"));

            // creo otro libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Effective Java", "Joshua Bloch", "Programming Languages"));

        } finally {
            // guardo la informacion.
            this.guardarInformacion();
        }

    }

    /**
     * Activa (inicia sesion) de un socio en el sistema.
     *
     * @param idSocio        a utilizar.
     * @param claveIngresada a validar.
     */
    public void iniciarSession(final int idSocio, final String claveIngresada) {

        // el numero de socio siempre es positivo.

        if (idSocio <= 0) {
            lanzarExcepcion("El número de socio no es valido!");
        }

        Socio socio = getSocio(idSocio);

        if (socio == null) {
            lanzarExcepcion("El socio no existe");
            return;
        }

        if (!claveIngresada.equals(socio.getClave())) {
            StdOut.println("La contraseña no es correcta");
            lanzarExcepcion("La contraseña no es correcta");
            return;
        }

        this.socio = socio;
        StdOut.println("Logeado! ");

    }

    /**
     * Lanzar un IllegalArgumentException
     *
     * @param mensaje el mensaje de la excepcion
     */

    private void lanzarExcepcion(String mensaje) {
        //Se lanza una excepción

        throw new IllegalArgumentException(mensaje);
    }

    /**
     * Obtiene un socio a base su id
     *
     * @param idSocio la id del socio
     * @return el socio
     */

    public Socio getSocio(int idSocio) {

        //Se obtiene un socio del array de socios, se resta una posición para que quede en orden

        return socios[idSocio - 1];
    }

    /**
     * Obtiene la instancia del socio logeado
     *
     * @return el socio
     */

    public Socio getSocioLogeado() {
        return socio;
    }

    /**
     * Cierra la session del Socio.
     */

    public void cerrarSession() {
        this.socio = null;
    }

    /**
     * Metodo que mueve un libro de los disponibles y lo ingresa a un Socio.
     *
     * @param isbn del libro a prestar.
     */
    public void realizarPrestamoLibro(final String isbn) throws IOException {
        // el socio debe estar activo.
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }

        // busco el libro.
        Libro libro = this.buscarLibro(isbn);

        // si no lo encontre, lo informo.
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        //El libro no debe estar en uso

        if (libro.enUso()) {
            throw new IllegalArgumentException("Lo siento el libro está en uso");
        }

        // agrego el libro al socio.
        this.socio.agregarLibro(libro);

        libro.marcarComoEnUso();

        // se actualiza la informacion de los archivos
        this.guardarInformacion();

        StdOut.println("Libro prestado... ");

    }

    /**
     * Obtiene un String que representa el listado completo de libros disponibles.
     *
     * @return the String con la informacion de los libros disponibles.
     */
    public String obtegerCatalogoLibros() {

        //Se recorre todos los libros

        StringBuilder sb = new StringBuilder();
        //Se crea un StringBuilder para anidar un String, al saber que se vana a
        // concatenar varias cosas se usa este objeto en vez de " "

        for (Libro libro : this.libros) {

            //Se imprime

            sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
            sb.append("Autor     : ").append(libro.getAutor()).append("\n");
            sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
            sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo que busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado.l
     */
    public Libro buscarLibro(final String isbn) {
        // recorro el arreglo de libros.
        for (Libro libro : this.libros) {
            // si lo encontre, retorno el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // no lo encontre, retorno null.
        return null;
    }

    /**
     * Actualiza la clave del socio logeado y luego guarda
     *
     * @param clave la clave que se actualizara
     */

    public void actualizarClaveYGuardar(String clave) {

        //Se actualiza la clave del socio logeado y luego se guarda

        try {
            socio.setClave(clave);
            guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el correo del socio logeado y luego guarda
     *
     * @param correo el correo que se actualizara
     */

    public void actualizarCorreoYGuardar(String correo) {

        //Se actualiza el correo del socio logeado y luego se guarda

        try {
            socio.setCorreo(correo);
            guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {

        // trato de leer los socios y los libros desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    public void guardarInformacion() throws IOException {

        // guardo los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // guardo los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }

    }

    /**
     * Obtiene los datos del socio logeado
     *
     * @return una cadena de texto que anida los datos del socio logeado
     */

    public String obtenerDatosSocioLogeado() {

        //Se obtiene los datos del socio logeado y se devuelven

        Socio socio = getSocioLogeado();

        return "Nombre: " + socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + socio.getCorreoElectronico();
    }

}
