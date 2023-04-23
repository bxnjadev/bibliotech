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

        //el while funciona como menu principal de la app

        while (!Objects.equals(opcion, "2")) {

            StdOut.println("""
                    [*] Bienvenido a BiblioTech [*]
                                    
                    [1] Iniciar Sesion
                    [2] Salir
                    """);
            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            // se esperan las opciones 1 o 2

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

        //Se espera una contraseña por pantalla

        StdOut.print("Ingrese su contrasenia: ");
        String contrasenia = StdIn.readLine();

        // intento el inicio de session
        try {
            sistema.iniciarSession(numeroSocio, contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error: " + ex.getMessage());
            return;
        }

        //Se meustra el menú principal
        mostrarMenuPrincipal(sistema, numeroSocio);
    }

    private static void mostrarMenuPrincipal(final Sistema sistema, int socioId) {
        String opcion = null;

        //Este while funciona como menu principal

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

            //Se lee una opción por consola

            opcion = StdIn.readLine();

            //Se esperan hasta 7 opciones

            switch (opcion) {
                case "1" -> mostrarMenuPrestamo(sistema);
                case "2" -> editarInformacion(sistema);
                case "3" -> calificarLibro(sistema, socioId);
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

        //Se muestran los catalogos de libro

        StdOut.println(sistema.obtegerCatalogoLibros());

        //Se espera por consola un ISBN

        StdOut.print("Ingrese el ISBN del libro a tomar prestado: ");
        String isbn = StdIn.readLine();

        try {

            //Se llama a la función que realiza un prestamo

            sistema.realizarPrestamoLibro(isbn);
        } catch (IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }

    }

    /**
     * Open and show the return book menu
     *
     * @param sistema the bibliotech system
     */

    private static void mostrarMenuDeDevueltaDeLibros(Sistema sistema) {

        StdOut.println("Bienvenido al sistema de devolución de libros: ");
        StdOut.println("Por favor ingresa el ISBN del libro que deseas devolver: ");

        //Se espera un isbn para regresar el libro

        String isbn = StdIn.readLine();

        Libro libroBuscado = sistema.buscarLibro(isbn);

        //Se verifica si el libro no es nulo

        if (libroBuscado == null) {
            StdOut.println("Lo siento este libro no existe");
            return;
        }

        //Se obtiene el socio logeado

        Socio socio = sistema.getSocioLogeado();

        //Se verifica si el socio no tiene el libro en ese caso se le avisa

        if (!socio.tieneLibro(isbn)) {
            StdOut.println("Lo siento no tienes este libro");
            return;
        }

        //El libro es sacado de uso y se borra

        libroBuscado.sacarDeUso();
        socio.borrarLibro(libroBuscado);

        StdOut.println("Has devuelto el libro!");

        try {

            //Por último se guarda

            sistema.guardarInformacion();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Abrir y mostrar los ajustes del socio
     *
     * @param sistema the bibliotech system
     */

    private static void editarInformacion(Sistema sistema) {

        String opcion = null;

        //Este while sirve como menu de edición de información

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

            //Se espera 3 opciones, editar correo actualizar clave o regresar

            switch (opcion) {
                case "1" -> modificarCorreo(sistema);
                case "2" -> modificarClave(sistema);
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

        //Se le pide una clave y luego se verifica si es la misma que se ingreso

        StdOut.println("Ingresa contraseña: ");

        String claveIngresada = StdIn.readLine();

        String clave = socio.getClave();
        return !clave.equals(claveIngresada);
    }

    /**
     * Abrir el menu de cambio de contraseña
     *
     * @param sistema el sistema de bibliotech
     */

    private static void modificarClave(Sistema sistema) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de contraseña de BiblioTech");

        //Se obtiene un socio logeado

        Socio socio = sistema.getSocioLogeado();

        //Se verifica la clase

        if (verificarClave(socio)) {
            StdOut.println("Lo siento, la contraseña ingresada no es valida");
        } else {

            //Este while será ejecutado hasta que ingrese bien la clave

            while (true) {

                StdOut.println("Ingresa una nueva contraseña: ");
                String nuevaClave = StdIn.readLine();

                //Se pide una contraseña

                StdOut.println("Ahora ingresa de nuevo contraseña: ");
                String nuevaClaveRepetida = StdIn.readLine();

                //Luego se pide otra

                if (nuevaClave.equals(nuevaClaveRepetida)) {
                    sistema.actualizarClaveYGuardar(nuevaClave);
                    break;
                }

                //Si son iguales se cambia y se guarda


                StdOut.println("Lo siento las contraseña no son iguales");
            }

        }

    }

    /**
     * Abrir el menu de edición de correo
     *
     * @param sistema el sistema de bibliotech
     */

    private static void modificarCorreo(Sistema sistema) {

        StdOut.println("" +
                "[*] Has accedido al sistema de cambio de correo electronico de BiblioTech" +
                "" +
                "Contraseña antigua: ");

        //Se obtiene el socio logeado

        Socio socio = sistema.getSocioLogeado();

        //Se verifica la clave

        if (verificarClave(socio)) {
            StdOut.println("La contrase no es correcta ");
        } else {

            //Este while es hasta que se haya puesto bien el correo

            while (true) {

                String correoViejo = socio.getCorreoElectronico();

                StdOut.println("Ingresa un nuevo correo: ");
                String nuevoCorreo = StdIn.readLine();

                //Se verifica si los correos son iguales, si es así te lo dice

                if (correoViejo.equals(nuevoCorreo)) {
                    StdOut.println("Este es el correo que ya tienes");
                } else {

                    //Sino son iguales se valida el correo, se actualiza y guarda

                    Utils.validarEmail(nuevoCorreo);
                    sistema.actualizarCorreoYGuardar(nuevoCorreo);
                    StdOut.println("Has cambiado el correo hasta " + nuevoCorreo);
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

        //Este while es hasta que se califican los libros

        while (true) {

            StdOut.println("Por favor escribe el ISBN del libro que deseas calificar: ");

            //Se obtiene un isbn

            String isbn = StdIn.readLine();

            //Se busca un libro a base de un isbn

            Libro libroBuscado = sistema.buscarLibro(isbn);

            //Se verifica si el libro buscado es nulo

            if (libroBuscado == null) {
                StdOut.println("Este libro no ha sido encontrado");
                break;
            }

            //Ahora se pide la calificacion

            StdOut.println("Ahora la calificación... ");
            int calificacion = StdIn.readInt();

            // Se verifica si la calificación esta en ese intervalo

            if (!(0 < calificacion && calificacion < 6)) {
                StdOut.println("Lo siento calificación no valida, recuerda que debes ");
                break;
            }

            //Se califica un libro dado una calificación y la id del socio

            libroBuscado.calificarLibro(calificacion, socioId);

            //Se intenta guardar la información

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

        //Se consulta un isbn para calificar

        String isbn = StdIn.readLine();

        //Luego se busca el libro a base del isbn

        Libro libroBuscado = sistema.buscarLibro(isbn);

        //Se verifica si es nulo

        if (libroBuscado == null) {
            StdOut.println("Lo siento libro no encontrado");
            return;
        }

        //Se obtiene el socio logeado en el sistema

        Socio partner = sistema.getSocioLogeado();

        //Se remueve la calificación

        libroBuscado.removerCalificacion(
                partner.getNumeroDeSocio()
        );

        //Por ultimo se intenta guardar los datos en el sistema

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

        //Se obtiene el isbn del libro que se quite obtener la información

        StdOut.println("Ingresa el isbn del libro del que quieras ver su información: ");
        String isbn = StdIn.readLine();

        //Se busca el libro

        Libro libroBuscado = sistema.buscarLibro(isbn);

        //Se verifica si es libro es nulo

        if (libroBuscado == null) {
            StdOut.println("Lo siento, libro no encontrado");
            return;
        }

        //Se muestran los datos por pantalla

        StdOut.println("Titulo: " + libroBuscado.getTitulo());
        StdOut.println("Autor: " + libroBuscado.getAutor());
        StdOut.println("ISBN: " + libroBuscado.getIsbn());
        StdOut.println("Categoria: " + libroBuscado.getCategoria());

        int calificacion = libroBuscado.getCalificacionFinal();

        if (calificacion == -1) {
            StdOut.println("Calificación: No se ha calificado este libro");
        } else {
            StdOut.println("Calificacion: " + calificacion);
        }

    }

}
