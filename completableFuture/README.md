# CompletableFuture

### What is a CompletableFuture?
* Used for asynchronous programming. Asynchronous programming is a means of writing non-blocking code by running a 
  task on a separate thread than the main application thread and notifying the main thread about its progress, 
  completion or failure.
* Runs a task on a separate thread than the main application thread, so the main thread is not blocked or doesn’t 
  wait for the completion of the task.
* Improved performance.

### What is a Future?
* Java’s Future API was introduced in Java 5 but has it’s limitations.
* Reference to the result of an async call.
* Can therefore be used to fetch the result when it becomes available.
* The concept of Future is similar to Promise in other languages like Javascript. It represents the result of a 
  computation that will be completed at a later point of time in future.
  

### How to execute a task asynchronously.
Use runAsync() or supplyAsync() to execute asynchronous tasks. Use thenApply(), thenAccept(), thenRun() as the callback
methods once runAsync() or supplyAsync() completes. 

You can chain callback methods.

# References
* https://www.callicoder.com/java-8-completablefuture-tutorial/
* https://www.baeldung.com/java-completablefuture

I did not cover Java 9, if you want to know what changed visit the below link:
* https://www.baeldung.com/java-9-completablefuture 

