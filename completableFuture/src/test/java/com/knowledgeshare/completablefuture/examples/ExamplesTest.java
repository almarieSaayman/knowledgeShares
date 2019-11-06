package com.knowledgeshare.completablefuture.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExamplesTest {

    private Examples cut;

    @BeforeEach
    void setUp() {
        cut = new Examples();
    }

    @Test
    @DisplayName("Blocks until the Future is complete")
    void get() throws ExecutionException, InterruptedException {
        cut.get();  // You will see that the test blocks forever, since the future is never completed
    }

    @Test
    @DisplayName("Way to manually complete a Future")
    void complete() throws ExecutionException, InterruptedException {
        // This can be used if you already know the result of the computation/function. This means the get() will never block.
        cut.complete();
    }

    @Test
    @DisplayName("Cancels the completion of the Future")
    void cancel() {
        assertThrows(CancellationException.class, () -> cut.cancel());
        // When cancel is used you will receive a CancellationException
    }

    @Test
    @DisplayName("Used when you want to run some background task without returning a result")
    void runAsync() throws ExecutionException, InterruptedException {
        // Used to simply execute some code asynchronously
        cut.runAsync(); // Used to execute some code asynchronously without returning the result
    }

    @Test
    @DisplayName("Used when you want to run some background task and return the result")
    void supplyAsync() throws ExecutionException, InterruptedException {
        cut.supplyAsync(); // Used to execute some code asynchronously and return the result
    }

    @Test
    @DisplayName("Use this to process and transform the result when it arrives")
    void thenApplyCallback() throws ExecutionException, InterruptedException {
        // Used for working with a result of the previous call
        cut.thenApplyCallback("Almarie");
    }

    @Test
    @DisplayName("Used for executing the callback on another thread")
    void thenApplyAsync() throws ExecutionException, InterruptedException {
        // Notice thenApply (the above test) executes on the same thread where supplyAsync is executed

        // To have more control over the thread that executes the callback you can use thenApplyAsync
        // You get Async variants of all thenXXXX methods
        cut.thenApplyAsync("Almarie"); // This is confusing!!
    }

    @Test
    @DisplayName("Used when you want to execute some piece of code after the Future completion without returning anything")
    void thenAcceptCallback() throws ExecutionException, InterruptedException {
        cut.thenAcceptCallback("Almarie");
    }

    @Test
    @DisplayName("Same as thenAccept(), but thenRun() does not have access to the Future’s result")
    void thenRunCallback() throws ExecutionException, InterruptedException {
        cut.thenRunCallback(5, 6);
    }

    @Test
    @DisplayName("You can also create a sequence of transformations")
    void thenApplyChainedCallbacks() throws ExecutionException, InterruptedException {
        cut.thenApplyChainedCallbacks("Almarie");
    }

    @Test
    @DisplayName("Used for combining two futures where one future is dependant on the other and flattening the result")
    void thenComposeCallback() throws ExecutionException, InterruptedException {
        // So, Rule of thumb here - If your callback function returns a CompletableFuture, and you want a flattened
        // result from the CompletableFuture chain (which in most cases you would), then use thenCompose()
        cut.thenComposeCallback("Almarie");
    }

    @Test
    @DisplayName("Used when you want futures to run independently and do something after both futures complete")
    void thenCombineCallback() throws ExecutionException, InterruptedException {
        // Assume you have a db table with user's weight and height, we can then calculate the user's BMI
        cut.thenCombineCallBack(58611L);
    }

    @Test
    @DisplayName("Used when you want a list of futures to run in parallel and do something after all futures complete")
    void allOf() throws ExecutionException, InterruptedException {
        cut.allOf();
    }

    @Test
    @DisplayName("Completes when any given future completes")
    void anyOf() throws ExecutionException, InterruptedException {
        cut.anyOf();
    }

    @Test
    @DisplayName("Gives you a chance to recover from errors generated from the original future in the callback chain")
    void exceptionally() throws ExecutionException, InterruptedException {
        // Say we want to return some default value if an exception occurs while getting the user address based on a user id

        System.out.println("Valid user id:");
        System.out.println("--------------");
        cut.exceptionally(5596L);

        System.out.println("\nInvalid user id:");
        System.out.println("----------------");
        cut.exceptionally(0L);
    }

    @Test
    @DisplayName("Used to recover from exceptions, but it is called whether or not an exception occurred")
    void handle() throws ExecutionException, InterruptedException {
        // Similar to exceptionally, but handle() will always execute and has access to the result

        System.out.println("Valid user id:");
        System.out.println("--------------");
        cut.handle(5596L);

        System.out.println("\nInvalid user id:");
        System.out.println("----------------");
        cut.handle(0L);
    }
}