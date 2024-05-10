package com.hoangtien2k3.race_condition.Solution_RaceCondition.Using_CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**

 **/

public class RaceCondition {

    public static void main(String... args) throws InterruptedException {
        final var shoppers = IntStream.range(0, 6)
                .mapToObj(Shopper::new) // Thread Shopper
                .toList();
        // Chạy toàn bộ các thread
        shoppers.forEach(Thread::start);
        // Chờ tất cả thread hoàn thành
        for (var shopper : shoppers) {
            shopper.join();
        }

        System.out.println("Total packs: " + Shopper.MASK_PACK_COUNT);
    }

}

class Shopper extends Thread {

    static int MASK_PACK_COUNT = 1;
    static CountDownLatch CDL = new CountDownLatch(3);

    Shopper(int i) {
        setName(i % 2 == 0 ? "Husband" : "Wife");
    }

    @Override
    public void run() {
        addMask(getName());
    }

    static void addMask(String threadName) {
        if ("husband".equalsIgnoreCase(threadName)) {
            synchronized (Shopper.class) {
                MASK_PACK_COUNT += 1;
                System.out.println("Husband adds 1 pack");
            }
            CDL.countDown();
            return;
        }
        waitAtBarrier();
        synchronized (Shopper.class) {
            MASK_PACK_COUNT *= 3;
            System.out.println("Wife multiple 3 times");
        }
    }

    static void waitAtBarrier() {
        try {
            CDL.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
