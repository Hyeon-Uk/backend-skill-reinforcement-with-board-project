package com.example.board.integration.member.service;

import com.example.board.member.domain.dto.auth.MemberSignIn;
import com.example.board.member.domain.exception.DuplicateException;
import com.example.board.member.repository.MemberJpaRepository;
import com.example.board.member.service.MemberAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberAuthServiceConcurrencyTest {
    @Autowired
    private MemberJpaRepository jpaRepository;
    @Autowired
    private MemberAuthService authService;

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAllInBatch();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 동시에_여러명이_같은아이디로_가입하면_한명만_가입() throws InterruptedException {
        //given
        int numOfThreads = 10;
        CountDownLatch countDownLatch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        AtomicBoolean otherException = new AtomicBoolean(false);

        //when
        for (int i = 0; i < numOfThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    authService.signIn(MemberSignIn.builder()
                            .id("sameId123")
                            .password("Abcdefg123!")
                            .passwordCheck("Abcdefg123!")
                            .nickname("nick" + finalI)
                            .name("Kim")
                            .email("rlagusdnr" + finalI + "@gmail.com")
                            .build());
                    successCount.getAndIncrement();
                } catch (DuplicateException e) {
                    failCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println("other exception occurs" + e);
                    otherException.set(true);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        assertAll("all",
                () -> assertFalse(otherException.get()),
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(numOfThreads - 1, failCount.get())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 동시에_여러명이_같은이메일로_가입하면_한명만_가입() throws InterruptedException {
        //given
        int numOfThreads = 10;
        CountDownLatch countDownLatch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        AtomicBoolean otherException = new AtomicBoolean(false);

        //when
        for (int i = 0; i < numOfThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    authService.signIn(MemberSignIn.builder()
                            .id("rlagusdnr" + finalI)
                            .password("Abcdefg123!")
                            .passwordCheck("Abcdefg123!")
                            .nickname("nick" + finalI)
                            .name("Kim")
                            .email("sameEmail@gmail.com")
                            .build());
                    successCount.getAndIncrement();
                } catch (DuplicateException e) {
                    failCount.getAndIncrement();
                } catch (Exception e) {
                    otherException.set(true);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        assertAll("all",
                () -> assertFalse(otherException.get()),
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(numOfThreads - 1, failCount.get())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 동시에_여러명이_같은닉네임으로_가입하면_한명만_가입() throws InterruptedException {
        //given
        int numOfThreads = 10;
        CountDownLatch countDownLatch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        AtomicBoolean otherException = new AtomicBoolean(false);

        //when
        for (int i = 0; i < numOfThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    authService.signIn(MemberSignIn.builder()
                            .id("rlagusdnr" + finalI)
                            .password("Abcdefg123!")
                            .passwordCheck("Abcdefg123!")
                            .nickname("same")
                            .name("Kim")
                            .email("rlagusdnr" + finalI + "@gmail.com")
                            .build());
                    successCount.getAndIncrement();
                } catch (DuplicateException e) {
                    failCount.getAndIncrement();
                } catch (Exception e) {
                    otherException.set(true);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        assertAll("all",
                () -> assertFalse(otherException.get()),
                () -> assertEquals(1, successCount.get()),
                () -> assertEquals(numOfThreads - 1, failCount.get())
        );
    }
}
