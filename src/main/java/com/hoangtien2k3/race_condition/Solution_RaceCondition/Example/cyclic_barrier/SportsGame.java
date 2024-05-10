package com.hoangtien2k3.race_condition.Solution_RaceCondition.Example.cyclic_barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SportsGame {
    private static final int NUM_ATHLETES = 4;
    private static final int NUM_OBSTACLES = 3;

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_ATHLETES, () -> System.out.print("All athletes passed the obstacle."));

        for (int i = 1; i <= NUM_OBSTACLES; i++) {
            System.out.println("Obstacle " + i + " started.");
            for(int j = 0; j < NUM_ATHLETES; j++) {
                new Thread(new Athlete(cyclicBarrier)).start();
            }
        }

    }

    static class Athlete implements Runnable {
        private final CyclicBarrier cyclicBarrier;

        Athlete(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }


        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is preparing for the obstacle.");
                Thread.sleep((long) (Math.random() * 3000));
                System.out.println(Thread.currentThread().getName() + " passed the obstacle and is waiting for others.");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


}
