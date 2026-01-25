package com.example.demo.excel.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import org.springframework.stereotype.Component;

@Component
public class ExcelDownloadLimiter {
    private final Semaphore semaphore = new Semaphore(3); // 동시 3개 제한

    public <T> T runWithLimit(Callable<T> task) {
        boolean acquired = semaphore.tryAcquire();
        if (!acquired) {
            throw new IllegalStateException("Too many concurrent downloads. Try again later.");
        }
        try {
            return task.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
}
