package com.hoangtien2k3.race_condition.RaceCondition;

import java.util.stream.IntStream;

/**
 Barrier:
    Ngăn chặn một nhóm các thread được thực thi cho đến khi tất cả các thread đều chạm tới barrier.

 Trong Java, có 2 class dựa trên tư tưởng của barrier :
    + CyclicBarrier
    + CountDownLatch

 barrier ta đã giải quyết được vấn đề. Không cần quan tâm đến việc OS sẽ thực thi thread khi nào,
 thời gian bao lâu, tần suất ra sao, kết quả cuối cùng sẽ không thay đổi.
 **/

public class RaceCondition {

    public static void main(String... args) throws InterruptedException {
        final var shoppers = IntStream.range(0, 6)
                .mapToObj(shopper -> new Shopper(shopper))
                .toList();
        // Chạy toàn bộ các thread
        shoppers.forEach(thread -> thread.start());
        // Chờ tất cả thread hoàn thành
        for (var shopper : shoppers) {
            shopper.join();
        }
        System.out.println("Total packs: " + Shopper.MASK_PACK_COUNT);
    }
}

class Shopper extends Thread {

    static int MASK_PACK_COUNT = 1;

    Shopper(int i) {
        setName(i % 2 == 0 ? "Husband" : "Wife");
    }

    @Override
    public void run() {
        addMask(getName());
    }

    static synchronized void addMask(String threadName) {
        if ("Husband".equalsIgnoreCase(threadName)) {
            MASK_PACK_COUNT += 1;
            System.out.println("Husband adds 1 pack");
            return;
        }
        MASK_PACK_COUNT *= 3;
        System.out.println("Wife multiple 3 times");
    }
}
