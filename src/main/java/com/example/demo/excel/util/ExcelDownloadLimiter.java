package com.example.demo.excel.util;

import com.example.demo.excel.exception.ExcelDownloadConcurrencyLimitExceededException;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import org.springframework.stereotype.Component;

@Component
public class ExcelDownloadLimiter {
    private final Semaphore semaphore = new Semaphore(3); // 동시 3개 제한

    public <T> T runWithLimit(Callable<T> task) {
        boolean acquired = semaphore.tryAcquire();
        if (!acquired) {
            throw new ExcelDownloadConcurrencyLimitExceededException("동시 다운로드 요청이 많습니다. 잠시 후 다시 시도해주세요.");
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
