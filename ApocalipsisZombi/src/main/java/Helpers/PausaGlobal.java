/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helpers;

/**
 *
 * @author hugos
 */
public class PausaGlobal {
    private static final PausaGlobal instancia = new PausaGlobal();
    private boolean pausado = false;

    private PausaGlobal() {}

    public static PausaGlobal getInstance() {
        return instancia;
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        try {
            Thread.sleep(100); // espera de 100ms antes de despertar hilos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // preservar la interrupción
        }
        pausado = false;
        notifyAll();
    }

    public synchronized void esperarSiPausado() {
        while (pausado) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Propagamos la interrupción para que el hilo se pueda detener si lo necesita
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized boolean estaPausado() {
        return pausado;
    }
}
