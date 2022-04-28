package ro.edu.ucv;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherMain {
    public static void main(String[] args) {
        Lock[] forks = new Lock[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosopher[] philosophers = new Philosopher[5];

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < philosophers.length; i++) {
            Lock leftFork = forks[i];
            Lock rightFork = forks[(i + 1) % forks.length];
            philosophers[i] = new Philosopher("Philosopher " + (i + 1), leftFork, rightFork);
            executorService.execute(philosophers[i]);
        }

        executorService.shutdown();
    }
}