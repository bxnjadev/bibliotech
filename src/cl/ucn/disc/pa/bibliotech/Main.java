/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import cl.ucn.disc.pa.bibliotech.services.Sistema;
import cl.ucn.disc.pa.bibliotech.services.Utils;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.IOException;
import java.util.Objects;

/**
 * The Main.
 *
 * @author Programacion Avanzada.
 */
public final class Main {

    /**
     * The main.
     *
     * @param args to use.
     * @throws IOException en caso de un error.
     */
    public static void main(final String[] args) throws IOException {

        // inicializacion del sistema.
        Sistema sistema = new Sistema();

        StdOut.println(sistema.obtegerCatalogoLibros());

        String opcion = null;
        while (!Objects.equals(opcion, "2")) {

            StdOut.println("""
                    [*] Bienvenido a BiblioTech [*]
                                    
                    [1] Iniciar Sesion
                    [2] Salir
                    """);
            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> iniciarSesion(sistema);
                case "2" -> StdOut.println("¡Hasta Pronto!");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Inicia la sesion del Socio en el Sistema.
     *
     * @param sistema a utilizar.
     */
    private static void iniciarSesion(final Sistema sistema) {
        StdOut.println("[*] Iniciar sesion en BiblioTech [*]");
        StdOut.print("Ingrese su numero de socio: ");
        int numeroSocio = StdIn.readInt();
        StdIn.readLine();

        StdOut.print("Ingrese su contrasenia: ");
        String contrasenia = StdIn.readLine();

        // intento el inicio de session
        try {
            sistema.iniciarSession(numeroSocio, contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error: " + ex.getMessage());
            return;
        }


        mostrarMenuPrincipal(sistema, numeroSocio);
    }

    private static void mostrarMenuPrincipal(final Sistema sistema, int partnerNumber) {
        String opcion = null;
        while (!Objects.equals(opcion, "4")) {
            StdOut.println("""
                    [*] BiblioTech [*]
                                        
                    [1] Prestamo de un libro
                    [2] Editar información
                    [3] Calificar libro
                    [4] Devolver un libro
                    [5] Mostrar información de un libro
                    [6] Remover una calificación a un libro
                                        
                    [7] Cerrar sesion
                    """);

            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> mostrarMenuPrestamo(sistema);
                case "2" -> editarInformacion(sistema, partnerNumber);
                case "3" -> calificarLibro(sistema, partnerNumber);
                case "4" -> mostrarMenuDeDevueltaDeLibros(sistema);
                case "5" -> mostrarLibro(sistema);
                case "6" -> removerCalification(sistema);
                case "7" -> sistema.cerrarSession();
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Open prestamo menu
     *
     * @param sistema the bibliotech system
     */

    private static void mostrarMenuPrestamo(Sistema sistema) {
        StdOut.println("[*] Préstamo de un Libro [*]");
        StdOut.println(sistema.obtegerCatalogoLibros());

        StdOut.print("Ingrese el ISBN del libro a tomar prestado: ");
        String isbn = StdIn.readLine();

        try {
            sistema.realizarPrestamoLibro(isbn);
        } catch (IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    /**
     * Open and show the return book menu
     *
     * @param bibliotechSystem the bibliotech system
     */

    private static void mostrarMenuDeDevueltaDeLibros(Sistema bibliotechSystem) {

        StdOut.println("Bienvenido al sistema de devolución de libros: ");
        StdOut.println("Por favor ingresa el ISBN del libro que deseas devolver: ");

        String isbn = StdIn.readLine();

        Libro libroBuscado = bibliotechSystem.buscarLibro(isbn);

        if (libroBuscado == null) {
            StdOut.println("Lo siento este libro no existe");
            return;
        }

        Socio socio = bibliotechSystem.getParnerLogged();

        if (!socio.hasBook(isbn)) {
            StdOut.println("Lo siento no tienes este libro");
            return;
        }

        libroBuscado.updateAsNotUsed();
        socio.deleteBook(libroBuscado);

        StdOut.println("Has devuelto el libro!");

        try {
            bibliotechSystem.guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Abrir y mostrar los ajustes del socio
     *
     * @param sistema       the bibliotech system
     * @param partnerNumber the partner number id
     */

    private static void editarInformacion(Sistema sistema, int partnerNumber) {

        String opcion = null;
        while (!Objects.equals(opcion, "3")) {

            StdOut.println("[*] Editar Perfil [*]");
            StdOut.println(sistema.obtenerDatosSocioLogeado());
            StdOut.println("""               
                    [1] Editar correo Electronico
                    [2] Editar Contraseña
                                        
                    [3] Volver atrás
                    """);
            StdOut.print("Escoja una opción: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> editMail(sistema, partnerNumber);
                case "2" -> updatePassword(sistema, partnerNumber);
                case "3" -> StdOut.println("Volviendo al menú anterior...");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Verificar si una contraseña es correcta
     *
     * @param socio el socio
     * @return devuelve verdadero si son iguales y falso si no es así
     */

    private static boolean verificarClave(Socio socio) {
        StdOut.println("Ingresa contraseña: ");

        String claveIngresada = StdIn.readLine();

        String clave = socio.getPassword();
        return !clave.equals(claveIngresada);
    }

    /**
     * Abrir el menu de cambio de contraseña
     *
     * @param sistema el sistema de bibliotech
     * @param idSocio la id del socio
     */

    private static void updatePassword(Sistema sistema, int idSocio) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de contraseña de BiblioTech");

        Socio socio = sistema.getPartner(idSocio);

        if (verificarClave(socio)) {
            StdOut.println("Lo siento, la contraseña ingresada no es valida");
        } else {

            while (true) {

                StdOut.println("Ingresa una nueva contraseña: ");
                String nuevaClave = StdIn.readLine();

                StdOut.println("Ahora ingresa de nuevo contraseña: ");
                String nuevaClaveRepetida = StdIn.readLine();

                if (nuevaClave.equals(nuevaClaveRepetida)) {
                    sistema.updatePassword(nuevaClave);
                    break;
                }

                StdOut.println("Lo siento las contraseña no son iguales");
            }

        }

    }

    /**
     * Abrir el menu de edición de correo
     *
     * @param sistema el sistema de bibliotech
     * @param socioId la id del socio
     */

    private static void editMail(Sistema sistema, int socioId) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de correo electronico de BiblioTech" +
                "" +
                "Contraseña antigua: ");

        Socio partner = sistema.getPartnerLogged();

        if (verificarClave(partner)) {
            StdOut.println("La contrase no es correcta ");
        } else {

            while (true) {

                String oldMail = partner.getCorreoElectronico();

                StdOut.println("Ingresa un nuevo correo: ");
                String newMail = StdIn.readLine();

                if (oldMail.equals(newMail)) {
                    StdOut.println("Este es el correo que ya tienes");
                } else {

                    Utils.validarEmail(newMail);
                    sistema.updateMail(newMail);
                    StdOut.println("Has cambiado el correo hasta " + newMail);
                    break;
                }

            }

        }

    }

    /**
     * Abrir el menu de calificación de libros
     *
     * @param sistema el sistema de bibliotech
     * @param socioId id del socio
     */

    private static void calificarLibro(Sistema sistema, int socioId) {

        StdOut.println("Bienvenido al sistema de calificaciones de BiblioTech ");

        while (true) {

            StdOut.println("Por favor escribe el ISBN del libro que deseas calificar: ");
            String isbn = StdIn.readLine();

            Libro libroBuscado = sistema.buscarLibro(isbn);

            if (libroBuscado == null) {
                StdOut.println("Este libro no ha sido encontrado");
                break;
            }

            StdOut.println("Ahora la calificación... ");
            int calificacion = StdIn.readInt();

            if (!(0 < calificacion && calificacion < 6)) {
                StdOut.println("Lo siento calificación no valida, recuerda que debes ");
                break;
            }

            libroBuscado.rankBook(calificacion, socioId);

            try {
                sistema.guardarInformacion();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StdOut.println("Has calificacado el libro " + libroBuscado.getTitulo() + " con " + calificacion + " estrellas");
            break;
        }

    }

    /**
     * Remove a calification in a book
     *
     * @param sistema el sistema de bibliotech
     */

    public static void removerCalification(Sistema sistema) {

        StdOut.println("Ingresa el isbn del libro que quieras quitar una calificación: ");
        String isbn = StdIn.readLine();

        Libro libroBuscado = sistema.buscarLibro(isbn);

        if (libroBuscado == null) {
            StdOut.println("Lo siento libro no encontrado");
            return;
        }

        Socio partner = sistema.getParnerLogged();

        libroBuscado.unRankBook(
                partner.getNumeroDeSocio()
        );

        try {
            sistema.guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StdOut.println("Has removido esa calificación ");

    }

    /**
     * Dado un isbn muestra un libro
     *
     * @param sistema el sistema de bibliotech
     */

    public static void mostrarLibro(Sistema sistema) {

        StdOut.println("Ingresa el isbn del libro del que quieras ver su información: ");
        String isbn = StdIn.readLine();

        Libro bookSearched = sistema.buscarLibro(isbn);

        if (bookSearched == null) {
            StdOut.println("Lo siento, libro no encontrado");
            return;
        }


        StdOut.println("Titulo: " + bookSearched.getTitulo());
        StdOut.println("Autor: " + bookSearched.getAutor());
        StdOut.println("ISBN: " + bookSearched.getIsbn());
        StdOut.println("Categoria: " + bookSearched.getCategoria());

        int calificacion = bookSearched.getFinalCalification();

        if (calificacion == -1) {
            StdOut.println("Calificación: No se ha calificado este libro");
        } else {
            StdOut.println("Calificacion: " + calificacion);
        }

    }

}
