package ro.edu.ucv;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Philosopher implements Runnable {
    private String name;
    private final Lock leftFork;
    private final Lock rightFork;

    public Philosopher(String name, Lock leftFork, Lock rightFork) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void think() {
        log("thinking");
    }

    public void eat() {
        //assume, eating requires some time.
        //let's put a random number
        try {
            log("eating");
            int eatingTime = getRandomEatingTime();
            TimeUnit.NANOSECONDS.sleep(eatingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (true) {
            keepThinkingAndEating();
        }
    }

    private void keepThinkingAndEating() {
        think();

        if (leftFork.tryLock()) {
            try {
                log("grabbed left fork");
                if (rightFork.tryLock()) {
                    try {
                        log("grabbed right fork");
                        eat();
                    } finally {
                        log("put down right fork");
                        rightFork.unlock();
                    }
                }
            } finally {
                log("put down left fork");
                leftFork.unlock();
            }
        }
    }

    private void log(String msg) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        String time = formatter.format(LocalDateTime.now());
        String thread = Thread.currentThread().getName();
        System.out.printf("%12s %s %s: %s%n", time, thread, name, msg);
        System.out.flush();
    }

    private int getRandomEatingTime() {
        Random random = new Random();
        return random.nextInt(500) + 50;
    }
}
