
package co.edu.uniquindio.poo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


/**
 * Clase para manejar la información de un curso
 * @author Área de programación UQ
 * @since 2024-01
 * 
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE) 
 */
public class Curso {
    private final String nombre;
    private final Collection<Estudiante> estudiantes;
    private final Collection<NotaParcial> notasParciales;

    /**
     * Método constructor de la clase Curso
     * 
     * @param nombre Nombre del curso
     */
    public Curso(String nombre) {
        assert nombre != null : "El nombre no puede ser nulo";
        this.nombre = nombre;
        estudiantes = new LinkedList<>();
        notasParciales = new LinkedList<>();
    }

    /**
     * Método para obtener el nombre del curso
     * 
     * @return Nombre del curso
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para agregar a un estudiante al curso
     * 
     * @param estudiante Estudiante que se desea agregar
     */
    public void agregarEstudiante(Estudiante estudiante) {
        assert validarNumeroIdentificacionExiste(estudiante.getNumeroIdentificacion()) == false
                : "El número de identificación ya existe.";
        estudiantes.add(estudiante);
    }

    /**
     * Método privado para determinar si ya existe un estudiante registro en el
     * mismo número de identificación
     * 
     * @param numeroIdentificacion Número de identificación a buscar
     * @return Valor boolean que indica si el número de identificación ya está o no
     *         registrado.
     */
    private boolean validarNumeroIdentificacionExiste(String numeroIdentificacion) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                return true;
            }
        }
        return false;
    }
    

    /**
     * Método para buscar un estudiante dado el número de indicación
     * 
     * @param numeroIdenficacion Número de identificación del estudiante a buscar
     * @return Estudiante con el número de indicación indicado o null
     */
    public Optional<Estudiante> obtenerEstudiante(String numeroIdenficacion) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNumeroIdentificacion().equals(numeroIdenficacion)) {
                return Optional.of(estudiante);
            }
        }
        return Optional.empty();
    }
    

    /**
     * Método para obtener la colección NO modificable de los estudiantes del curso
     * 
     * @return colección NO modificable de los estudiantes del curso
     */
    public Collection<Estudiante> getEstudiantes() {
        return Collections.unmodifiableCollection(estudiantes);
    }

    /**
     * Método para obtener la colección NO modificable de los estudiantes del curso
     * en orden alfabético
     * 
     * @return colección NO modificable de los estudiantes del curso en
     *         orden alfabético
     */
    public Collection<Estudiante> obtenerListadoAlfabetico() {
        List<Estudiante> estudiantesOrdenados = new ArrayList<>(estudiantes);
        estudiantesOrdenados.sort(Comparator.comparing(Estudiante::getNombres));
        return Collections.unmodifiableCollection(estudiantesOrdenados);
    }
    
    /**
     * Método para obtener la colección NO modificable de los estudiantes del curso
     * en orden descendente de la edad
     * 
     * @return la colección NO modificable de los estudiantes del curso registrados
     *         en el curso descendente por edad.
     */
    public Collection<Estudiante> obtenerListadoEdadDescendente() {
        List<Estudiante> listaEstudiantes = new ArrayList<>(estudiantes);
        
        for (int i = 0; i < listaEstudiantes.size() - 1; i++) {
            for (int j = 0; j < listaEstudiantes.size() - i - 1; j++) {
                Estudiante estudianteActual = listaEstudiantes.get(j);
                Estudiante siguienteEstudiante = listaEstudiantes.get(j + 1);
                if (estudianteActual.getEdad() < siguienteEstudiante.getEdad()) {
                    Collections.swap(listaEstudiantes, j, j + 1);
                }
            }
        }
        
        return Collections.unmodifiableCollection(listaEstudiantes);
    }

    /**
     * Método para obtener la colección NO modificable de los estudiantes del curso
     * que son menores de edad
     * 
     * @return la colección NO modificable de los estudiantes del curso que
     *         son menores de edad.
     */
    public Collection<Estudiante> obtenerListadoMenoresEdad() {
        List<Estudiante> menoresEdad = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getEdad() < 18) {
                menoresEdad.add(estudiante);
            }
        }
        return Collections.unmodifiableCollection(menoresEdad);
    }
    
    /**
     * Método para adicionar una nota parcial
     * Se puede validar que la suma de los porcentajes no sobrepase en ningún
     * momento 1.0 (100%)
     * 
     * @param notaParcial nota parcial que se desea adicionar al curso
     */
    public void adicionarNotaParcial(NotaParcial notaParcial) {
        notasParciales.add(notaParcial);
    }

    /**
     * Método para obtener una nota parcial dado el nombre de la nota parcial
     * @param nombreNotaParcial nombre de la nota parcial a buscar
     * @return nota parcial encontrada o un excepción de no entrada.
     */
    public NotaParcial getNotaParcial(String nombreNotaParcial) {
        for (NotaParcial notaParcial : notasParciales) {
            if (notaParcial.nombre().equals(nombreNotaParcial)) {
                return notaParcial;
            }
        }
        throw new NoSuchElementException("No se encontró la nota parcial con el nombre especificado.");
    }
    
    /**
     * Método que obtiene la colección de los estudiantes con mayor nota.
     * @return colección de los estudiantes con la mayor nota.
     */
    public Collection<Estudiante> obtenerListadoMayorNota() {
        double mayorNota = obtenerMayorNota();
        List<Estudiante> estudiantesConMayorNota = new ArrayList<>();
    
        for (Estudiante estudiante : estudiantes) {
            if (Math.abs(mayorNota - estudiante.getDefinitiva()) <= App.PRECISION) {
                estudiantesConMayorNota.add(estudiante);
            }
        }
    
        return Collections.unmodifiableCollection(estudiantesConMayorNota);
    }
    
    /**
     * Método de apoyo (privado) que determinar el valor de la mayor nota definitiva
     * @return mayor nota definitiva de los estudiantes del curso.
     */
    private double obtenerMayorNota() {
        double mayorNota = Double.MIN_VALUE;
    
        for (Estudiante estudiante : estudiantes) {
            double definitiva = estudiante.getDefinitiva();
            if (definitiva > mayorNota) {
                mayorNota = definitiva;
            }
        }
    
        return mayorNota;
    }
    
    /**
     * Método para obtener la colección de los estudiantes que perdieron en orden alfabético.
     * @return colección de los estudiantes que perdieron en orden alfabético.
     */
    public Collection<Estudiante> obtenerListadoAlfabeticoPerdieron() {
        List<Estudiante> perdieron = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getDefinitiva() < App.MINIMA_NOTA) {
                perdieron.add(estudiante);
            }
        }
        Comparator<Estudiante> comparador = Comparator.comparing(Estudiante::getNombres);
        perdieron.sort(comparador);
        return Collections.unmodifiableCollection(perdieron);
    }
    

    /**
     * Método para validar que la suma de los porcentajes de las notas parciales esa 1.0 (100%)
     * @return verdadero si la suma de los porcentajes es 1.0 (100%) o tan cercano como la precisión indicada.
     */
    public boolean validarPorcentajes() {
        double sumaPorcentajes = 0.0;
        for (NotaParcial notaParcial : notasParciales) {
            sumaPorcentajes += notaParcial.porcentaje();
        }
        return Math.abs(1.0 - sumaPorcentajes) <= App.PRECISION;
    }
    

}
