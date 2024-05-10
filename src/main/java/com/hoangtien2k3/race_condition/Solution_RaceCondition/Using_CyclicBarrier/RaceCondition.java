package com.hoangtien2k3.race_condition.Solution_RaceCondition.Using_CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

/**
 # Nó được gọi là CyclicBarrier vì có thể được sử dụng lại khi tất cả các thread được giải phóng,
 hoặc khi gọi method reset.


 # Có một vài method hữu ích trong CyclicBarrier:

 // Tổng số thread để giải phóng barrier: 7
 int getParties();

 // Số lượng thread đang chờ: số người đã có mặt trên sân
 int getNumberWaiting();

 // Reset về trạng thái ban đầu, getNumberWaiting() = 0
 void reset();
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
    static CyclicBarrier BARRIER = new CyclicBarrier(6);

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
            waitAtBarrier();
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
            BARRIER.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
