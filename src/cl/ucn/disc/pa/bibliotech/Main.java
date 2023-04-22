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


        showMainMenu(sistema, numeroSocio);
    }

    private static void showMainMenu(final Sistema sistema, int partnerNumber) {
        String opcion = null;
        while (!Objects.equals(opcion, "4")) {
            StdOut.println("""
                    [*] BiblioTech [*]
                                        
                    [1] Prestamo de un libro
                    [2] Editar información
                    [3] Calificar libro
                    [4] Devolver un libro
                    [5] Mostrar información de un libro
                                        
                    [4] Cerrar sesion
                    """);

            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> menuPrestamo(sistema);
                case "2" -> editarInformacion(sistema, partnerNumber);
                case "3" -> rankBook(sistema, partnerNumber);
                case "4" -> openMenuReturnBook(sistema);
                case "5" -> showDataBook(sistema);
                case "6" -> sistema.cerrarSession();
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    private static void menuPrestamo(Sistema sistema) {
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

    private static void openMenuReturnBook(Sistema bibliotechSystem) {

        StdOut.println("Bienvenido al sistema de devolución de libros: ");
        StdOut.println("Por favor ingresa el ISBN del libro que deseas devolver: ");

        String isbn = StdIn.readLine();

        Libro bookSearched = bibliotechSystem.buscarLibro(isbn);

        if (bookSearched == null) {
            StdOut.println("Lo siento este libro no existe");
            return;
        }

        Socio partner = bibliotechSystem.getParnerLogged();

        if (!partner.hasBook(isbn)) {
            StdOut.println("Lo siento no tienes este libro");
            return;
        }

        bookSearched.updateAsNotUsed();
        partner.deleteBook(bookSearched);

        StdOut.println("Has devuelto el libro!");

        try {
            bibliotechSystem.guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    private static boolean checkPassword(Socio partner) {
        StdOut.println("Ingresa contraseña: ");

        String passwordEntered = StdIn.readLine();

        String password = partner.getPassword();
        return !password.equals(passwordEntered);
    }

    private static void updatePassword(Sistema bibliotechSystem, int partnerNumber) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de contraseña de BiblioTech");

        Socio partner = bibliotechSystem.getPartner(partnerNumber);

        if (checkPassword(partner)) {
            StdOut.println("Lo siento, la contraseña ingresada no es valida");
        } else {

            while (true) {

                StdOut.println("Ingresa una nueva contraseña: ");
                String newPassword = StdIn.readLine();

                StdOut.println("Ahora ingresa de nuevo contraseña: ");
                String newPasswordRepeated = StdIn.readLine();

                if (newPassword.equals(newPasswordRepeated)) {
                    bibliotechSystem.updatePassword(newPassword);
                    break;
                }

                StdOut.println("Lo siento las contraseña no son iguales");
            }

        }

    }

    private static void editMail(Sistema bibliotechSystem, int partnerNumber) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de correo electronico de BiblioTech" +
                "" +
                "Contraseña antigua: ");

        Socio partner = bibliotechSystem.getPartner(partnerNumber);

        if (checkPassword(partner)) {
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
                    bibliotechSystem.updateMail(newMail);
                    StdOut.println("Has cambiado el correo hasta " + newMail);
                    break;
                }

            }

        }

    }

    private static void rankBook(Sistema bibliotechSystem, int partnerNumber) {

        StdOut.println("Bienvenido al sistema de calificaciones de BiblioTech ");

        while (true) {

            StdOut.println("Por favor escribe el ISBN del libro que deseas calificar: ");
            String isbn = StdIn.readLine();

            Libro bookSearched = bibliotechSystem.buscarLibro(isbn);

            if (bookSearched == null) {
                StdOut.println("Este libro no ha sido encontrado");
                break;
            }

            int calification = StdIn.readInt();

            if (!(0 < calification && calification < 6)) {
                StdOut.println("Lo siento calificación no valida, recuerda que debes ");
                break;
            }

            bookSearched.rankBook(calification, partnerNumber);
            StdOut.println("Has calificacado el libro " + bookSearched.getTitulo() + " con " + calification + " estrellas");

        }

    }

    public static void showDataBook(Sistema bibliotechSystem) {

        StdOut.println("Ingresa el isbn del libro del que quieras ver su información: ");
        String isbn = StdIn.readLine();

        Libro bookSearched = bibliotechSystem.buscarLibro(isbn);

        if (bookSearched == null) {
            StdOut.println("Lo siento, libro no encontrado");
            return;
        }


        StdOut.println("Titulo: " + bookSearched.getTitulo());
        StdOut.println("Autor: " + bookSearched.getAutor());
        StdOut.println("ISBN: " + bookSearched.getIsbn());
        StdOut.println("Categoria: " + bookSearched.getCategoria());

        int finalCalification = bookSearched.getFinalCalification();

        if (finalCalification == -1) {
            StdOut.println("Calificación: No se ha calificado este libro");
        } else {
            StdOut.println("Calificacion: " + bookSearched.getFinalCalification());
        }

    }

}
