package test;

public class Main {
    @LogExecutionTime
    public void doSomething() {
        System.out.println("Doing something...");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.doSomething();
    }
}
