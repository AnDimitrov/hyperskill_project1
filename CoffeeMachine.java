package machine;

import java.util.Scanner;

public class CoffeeMachine {
    private int money;
    private int water;
    private int milk;
    private int coffee;
    private int cups;
    private enum State {
        READY,
        BUY,
        FILL,
        SHUTDOWN
    }
    private enum FillState {
        ADD_WATER,
        ADD_MILK,
        ADD_COFFEE,
        ADD_CUPS,
        END_FILL
    }
    private State state;
    private FillState fillState;

    public CoffeeMachine(int money, int water, int milk, int coffee, int cups) {
        this.money = money;
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cups = cups;
    }

    void init() {
        ready();
    }

    void ready() {
        setState(State.READY);
        System.out.println();
        System.out.print("Write action (buy, fill, take, remaining, exit): ");
    }

    void command(String input) {
        switch (this.state) {
            case READY:
                switch (input) {
                    case "remaining":
                        printRemaining();
                        break;
                    case "buy":
                        System.out.println();
                        if (cups > 0) {
                            System.out.print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
                            setState(State.BUY);
                        } else {
                            System.out.println("Sorry, not enough disposable cups!");
                            setState(State.READY);
                        }
                        break;
                    case "fill":
                        setState(State.FILL);
                        setFillState(FillState.ADD_WATER);
                        fill("0");
                        break;
                    case "take":
                        takeMoney();
                        break;
                    case "exit":
                        setState(State.SHUTDOWN);
                        break;
                    default:
                        ready();
                        break;
                }
                break;
            case BUY:
                int waterNeed;
                int milkNeed;
                int coffeeNeed;
                int cost;
                switch (input) {
                    case "1":
                        waterNeed = 250;
                        milkNeed = 0;
                        coffeeNeed = 16;
                        cost = 4;
                        break;
                    case "2":
                        waterNeed = 350;
                        milkNeed = 75;
                        coffeeNeed = 20;
                        cost = 7;
                        break;
                    case "3":
                        waterNeed = 200;
                        milkNeed = 100;
                        coffeeNeed = 12;
                        cost = 6;
                        break;
                    default:
                        waterNeed = 0;
                        milkNeed = 0;
                        coffeeNeed = 0;
                        cost = 0;
                        break;
                }

                if (water < waterNeed) {
                    System.out.println("Sorry, not enough water!");
                } else if (milk < milkNeed) {
                    System.out.println("Sorry, not enough milk!");
                } else if (coffee < coffeeNeed) {
                    System.out.println("Sorry, not enough coffee!");
                } else if (cost != 0) {
                    System.out.println("I have enough resources, making you a coffee!");
                    water -= waterNeed;
                    milk -= milkNeed;
                    coffee -= coffeeNeed;
                    money += cost;
                    cups -= 1;
                }
                ready();
                break;
            case FILL:
                setState(State.FILL);
                fill(input);
                break;
            default:
                break;
        }
    }

    void printRemaining() {
        System.out.println();
        System.out.println("The coffee machine has:");
        System.out.println(water + " of water");
        System.out.println(milk + " of milk");
        System.out.println(coffee + " of coffee beans");
        System.out.println(cups + " of disposable cups");
        System.out.println("$" + money + " of money");
        ready();
    }

    void fill(String input) {
        switch (fillState) {
            case ADD_WATER:
                System.out.println();
                System.out.print("Write how many ml of water do you want to add: ");
                setFillState(FillState.ADD_MILK);
                break;
            case ADD_MILK:
                water += Integer.parseInt(input);
                System.out.print("Write how many ml of milk do you want to add: ");
                setFillState(FillState.ADD_COFFEE);
                break;
            case ADD_COFFEE:
                milk += Integer.parseInt(input);
                System.out.print("Write how many grams of coffee beans do you want to add: ");
                setFillState(FillState.ADD_CUPS);
                break;
            case ADD_CUPS:
                coffee += Integer.parseInt(input);
                System.out.print("Write how many disposable cups of coffee do you want to add: ");
                setFillState(FillState.END_FILL);
                break;
            case END_FILL:
                cups += Integer.parseInt(input);
            default:
                ready();
        }
    }

    void takeMoney() {
        System.out.println();
        System.out.println("I gave you $" + money);
        money = 0;
        ready();
    }

    State getState() {
        return this.state;
    }

    void setState(State state) {
        this.state = state;
    }

    void setFillState(FillState fillState) {
        this.fillState = fillState;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CoffeeMachine coffeeMachine = new CoffeeMachine(550, 400, 540, 120, 9);
        coffeeMachine.init();
        do {
            coffeeMachine.command(sc.next());
        }
        while (coffeeMachine.getState() != State.SHUTDOWN);
    }
}
