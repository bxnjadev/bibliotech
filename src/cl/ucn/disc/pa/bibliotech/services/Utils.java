/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase que reune los metodos utilitarios.
 *
 * @author Diego Urrutia-Astorga.
 */
public final class Utils {

    /**
     * The Email validator.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    /**
     * Constructor privado: nadie puede instanciar esta clase.
     */
    private Utils() {
        // nothing here
    }

    /**
     * Add theObject to theStaticArray.
     *
     * @param theStaticArray the array.
     * @param theObject      the object to append.
     * @param <T>            generic to use.
     * @return the static array.
     */
    public static <T> T[] append(T[] theStaticArray, T theObject) {
        // new arraylist
        List<T> theList = new ArrayList<>();
        // copy all the items from [] to the list
        Collections.addAll(theList, theStaticArray);
        // add the object
        theList.add(theObject);
        // return the static array
        return theList.toArray(theStaticArray);
    }

    /**
     * remove theObject to theStaticArray
     *
     * @param array  the array
     * @param object the object to append
     * @param <T>    generic use
     * @return the static array
     */

    public static <T> T[] removeElement(T[] array, T object) {

        List<T> list = new ArrayList<>();

        Collections.addAll(list, array);

        list.remove(object);

        return list.toArray(array);
    }

    /**
     * Valida un correo electronico, en caso de no ser valido se lanza una Exception.
     *
     * @param email a validar.
     */
    public static void validarEmail(final String email) {
        // el correo debe ser estructuralmente valido
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Correo Electronico no valido: " + email);
        }
    }

    /**
     * Valida una clave, en caso de no ser valida lanza una Exception.
     *
     * @param clave a validar
     */

    public static void validarClave(String clave) {

        if (clave == null || clave.isEmpty()) {
            throw new IllegalArgumentException("La clave está vacia.");
        }

        validarLongitud(clave, 3, 20,
                "La clave es muy larga ", "La clave es muy corta");

        boolean contieneNumeros = false;

        for (int i = 0; i < clave.length(); i++) {
            char caracter = clave.charAt(i);

            if (Character.isDigit(caracter)) {
                contieneNumeros = true;
            }

        }

        if (!contieneNumeros) {
            throw new IllegalArgumentException("La clave debe contener números");
        }


    }

    /**
     * Realiza una validación de longitud de un String, en caso de no ser valida lanza una Exception.
     *
     * @param valor              el valor a validar
     * @param minimo             el valor minimo de caracteres
     * @param maximo             el valor máximo de caracteres
     * @param mensajeErrorMaximo mensaje de error si se supera el número máximo de caracteres
     * @param mensajeErrorMinimo mensaje de error si se supera el número mínimo de caracteres
     */

    public static void validarLongitud(String valor, int minimo, int maximo, String mensajeErrorMaximo,
                                       String mensajeErrorMinimo) {

        int longitud = valor.length();

        if (maximo > longitud) {
            throw new IllegalArgumentException(mensajeErrorMaximo);
        }

        if (longitud < minimo) {
            throw new IllegalArgumentException(mensajeErrorMinimo);
        }

    }

    /**
     * Valida un nombre u apellido
     *
     * @param valor el nombre o apellido
     */

    public static void validarNombreOApellido(String valor) {

        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("El nombre o apellido no pueden ser nulos.");
        }

        validarLongitud(valor, 2, 12,
                "La clave es muy larga ", "La clave es muy corta");

    }

    /**
     * Valida el número de socio
     *
     * @param numeroSocio el número de socio
     */

    public static void validarNumeroSocio(int numeroSocio) {
        if (numeroSocio < 0) {
            throw new IllegalArgumentException("El número de socio debe ser negativo");
        }
    }

    public static void validarString(String isbn, String mnesajeError) {
        if (isbn == null) {
            throw new IllegalArgumentException("El isbn es nulo");
        }
    }

}
