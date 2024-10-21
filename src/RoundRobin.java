import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {
    public static void main(String[] args) {

        String[] trabajos = {"A", "B", "C", "D", "E"};
        int[] rafagaCPU = {3, 1, 3, 4, 2};
        int[] tiempoLlegada = {2, 4, 0, 1, 3};
        int quantum = 3;

        Job[] jobArray = new Job[trabajos.length];
        for (int i = 0; i < trabajos.length; i++) {
            jobArray[i] = new Job(trabajos[i], tiempoLlegada[i], rafagaCPU[i]);
        }

        Queue<Job> queue = new LinkedList<>();
        int tiempoActual = 0;
        int[] tiempoEspera = new int[jobArray.length];
        int[] tiempoRetorno = new int[jobArray.length];

        int completed = 0;
        while (completed < trabajos.length) {
            for (Job job : jobArray) {
                if (job.tiempoLlegada <= tiempoActual && job.rafaga > 0 && !queue.contains(job)) {
                    queue.add(job);
                }
            }

            if (!queue.isEmpty()) {
                Job currentJob = queue.poll();
                int tiempoUsado = Math.min(currentJob.rafaga, quantum);
                tiempoActual += tiempoUsado;
                currentJob.rafaga -= tiempoUsado;

                if (currentJob.rafaga == 0) {
                    tiempoRetorno[completed] = tiempoActual;
                    tiempoEspera[completed] = tiempoRetorno[completed] - currentJob.tiempoLlegada - rafagaCPU[completed];
                    completed++;
                } else {
                    queue.add(currentJob);
                }
            } else {
                tiempoActual++;
            }
        }

        double tiempoMedioEspera = 0;
        double tiempoMedioRetorno = 0;
        for (int i = 0; i < trabajos.length; i++) {
            tiempoMedioEspera += tiempoEspera[i];
            tiempoMedioRetorno += tiempoRetorno[i];
        }
        tiempoMedioEspera /= trabajos.length;
        tiempoMedioRetorno /= trabajos.length;

        System.out.println("Trabajo\t\tTiempo de Llegada\tRáfaga CPU\tTiempo de Espera\tTiempo de Retorno");
        for (int i = 0; i < trabajos.length; i++) {
            System.out.println(jobArray[i].nombre + "\t\t\t\t" + jobArray[i].tiempoLlegada + "\t\t\t\t" + rafagaCPU[i] + "\t\t\t\t" + tiempoEspera[i] + "\t\t\t\t" + tiempoRetorno[i]);
        }
        System.out.println("Tiempo Medio de Espera: " + tiempoMedioEspera);
        System.out.println("Tiempo Medio de Retorno: " + tiempoMedioRetorno);
    }
}