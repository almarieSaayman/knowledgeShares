package com.knowledgeshare.completablefuture.examples;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Examples {

    public String get() throws ExecutionException, InterruptedException {
        System.out.println("Main thread id: " + Thread.currentThread().getId());

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        return completableFuture.get();
    }

    public void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "Task value";
        });

        completableFuture.complete("Early completion value");
        System.out.println(completableFuture.get());
    }

    public void cancel() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task value";
        });

        completableFuture.cancel(true);
        System.out.println(completableFuture.get());
    }

    public void runAsync() throws ExecutionException, InterruptedException {
        System.out.println("Main thread id: " + Thread.currentThread().getId());

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("I'll run in a separate thread than the main thread, current thread id: " + Thread.currentThread().getId());
            // Notice this just prints the string and returns nothing (CompletableFuture<Void>)
        });

        completableFuture.get();
    }

    public void supplyAsync() throws ExecutionException, InterruptedException {
        System.out.println("Main thread id: " + Thread.currentThread().getId());

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5); // Some other task that executes for 5 seconds as an example
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "I'll run in a separate thread than the main thread, current thread id: " + Thread.currentThread().getId();
        });

        System.out.println(completableFuture.get());
    }

    public void thenApplyCallback(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supply thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return name;
        }).thenApply(futureResult -> {
            System.out.println("Apply thread id: " + Thread.currentThread().getId());

            return "Hello " + futureResult; // Notice thenApply is only executed after supplyAsync completed
        });

        System.out.println(completableFuture.get());
    }

    public void thenApplyAsync(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supply thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return name;
        }).thenApplyAsync(futureResult -> {
            // Essentially this execution should not wait for the result of supplyAsync
            System.out.println("Apply async thread id: " + Thread.currentThread().getId());

            return "Hello " + futureResult;
        });

        System.out.println(completableFuture.get());
    }

    public void thenAcceptCallback(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supply thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return name;
        }).thenAccept(futureResult -> {
            System.out.println("Accept thread id: " + Thread.currentThread().getId());

            System.out.println("Hello " + futureResult); // Notice nothing is being returned here since you cannot return anything here
        });

        completableFuture.get();
    }

    public void thenRunCallback(int x, int y) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supply thread id: " + Thread.currentThread().getId());

            int sum = x + y;
            System.out.println(x + " + " + y + " = " + sum);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return sum;
        }).thenRun(() -> {
            System.out.println("Run thread id: " + Thread.currentThread().getId());

            System.out.println("Calculation is complete");
        });

        completableFuture.get();
    }

    public void thenApplyChainedCallbacks(String name) throws ExecutionException, InterruptedException {
        // Attach your callback chain
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supply thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return name;
        }).thenApply(futureResult -> {
            System.out.println("First apply thread id: " + Thread.currentThread().getId());

            return "Hello " + futureResult;
        }).thenApply(callBackResult -> {
            System.out.println("Second apply thread id: " + Thread.currentThread().getId());

            return callBackResult + "! How are you?";
        });

        System.out.println(completableFuture.get());
    }

    public void thenComposeCallback(String userName) throws ExecutionException, InterruptedException {
        // If you use thenApply the final result with be a nested CompletableFuture
        System.out.println("Nested completable future");
        System.out.println("-------------------------");
        CompletableFuture<CompletableFuture<Double>> result = getUserId(userName).thenApply(userId -> getUserCreditRating(userId));
        CompletableFuture<Double> userCreditRatingFuture = result.get();
        System.out.println(String.format("Final credit rating for %s is %s", userName, userCreditRatingFuture.get()));

        //Use thenCompose to flatten the result
        System.out.println("\nFlattened completable future");
        System.out.println("----------------------------");
        CompletableFuture<Double> flattenedResult = getUserId(userName).thenCompose(userId -> getUserCreditRating(userId));
        System.out.println(String.format("Final credit rating for %s is %s", userName, flattenedResult.get()));
    }

    CompletableFuture<Long> getUserId(String userName) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("User name thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5); // Some other task that get the user id based on the user name as an example
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return 147L;
        });
    }

    CompletableFuture<Double> getUserCreditRating(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Credit rating thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5); // Some other task that get the user credit rating based on the user id as an example
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return 10.5;
        });
    }

    public void thenCombineCallBack(Long userId) throws ExecutionException, InterruptedException {
        CompletableFuture<Double> bmi = getWeightInKg(userId).thenCombine(getHeightInCm(userId), (weight, height) -> {
            Double heightInMeter = height / 100;
            return weight / (heightInMeter * heightInMeter);
        });
        // Notice that getWeightInKg and getHeightInCm runs simultaneously on different threads

        System.out.println(String.format("User with id %s has a BMI of %s", userId, bmi.get()));
    }

    private CompletableFuture<Double> getWeightInKg(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Weight thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5); // Some other task that gets the user weight based on the user id as an example
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return 65.0;
        });
    }

    private CompletableFuture<Double> getHeightInCm(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Height thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5); // Some other task that gets the user height based on the user id as an example
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return 177.8;
        });
    }

    public void allOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> futureOne = futureOne();
        CompletableFuture<String> futureTwo = futureTwo();
        CompletableFuture<String> futureThree = futureThree();

        CompletableFuture<Void> combined = CompletableFuture.allOf(futureOne, futureTwo, futureThree);
        // All of above will run all the futures simultaneously

        // Notice above the return type is Void, you will have to manually get the result from each future like this
        String manualResult = futureOne.get() + futureTwo.get() + futureThree.get();
        System.out.println("Manual result: " + manualResult);

        // OR you can use join to do the above automatically for you
        String joinResult = Stream.of(futureOne, futureTwo, futureThree)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(""));
        System.out.println("Join result: " + joinResult);
    }

    private CompletableFuture<String> futureOne() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Future one thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "How ";
        });
    }

    private CompletableFuture<String> futureTwo() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Future two thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "are ";
        });
    }

    private CompletableFuture<String> futureThree() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Future three thread id: " + Thread.currentThread().getId());

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "you?";
        });
    }

    public void anyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(futureOne(), futureTwo(), futureThree());
        System.out.println("Result of the future that completed first: " + anyFuture.get());
        // futureThree completed first since it only took 3 seconds where the other 2 futures took longer than 3 seconds
    }

    public void exceptionally(Long userId) throws ExecutionException, InterruptedException {
        CompletableFuture<String> userAddress = CompletableFuture.supplyAsync(() -> {
            if (userId < 1) {
                throw new IllegalArgumentException("User id cannot be less than 1");
            }

            return "FNB Building, 1 Enterprise Rd, Fairland, Johannesburg, 2170";
        }).exceptionally(ex -> {
            System.out.println(String.format("Could not retrieve user address for user with id: %s, exception: %s", userId, ex));
            return "Not available";
        });

        System.out.println(userAddress.get());
    }

    public void handle(Long userId) throws ExecutionException, InterruptedException {
        CompletableFuture<String> userAddress = CompletableFuture.supplyAsync(() -> {
            if (userId < 1) {
                throw new IllegalArgumentException("User id cannot be less than 1");
            }

            return "FNB Building, 1 Enterprise Rd, Fairland, Johannesburg, 2170";
        }).handle((result, ex) -> { // Here you will get the result from the above future as well, and this will ALWAYS execute
            // If an exception occurs, then the res argument will be null, otherwise, the ex argument will be null
            System.out.println("Result from future: " + result);

            if (ex != null) {
                System.out.println(String.format("Could not retrieve user address for user with id: %s, exception: %s", userId, ex));
                return "Not available";
            }

            return result;
        });

        System.out.println(userAddress.get());
    }
}
