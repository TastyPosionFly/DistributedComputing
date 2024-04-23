package DI_IOC;

import java.util.Scanner;

public class TestRestaurant {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        Scanner scanner = new Scanner(System.in);
        System.out.println("����������е��" + "\n");
        String foodName = scanner.nextLine();
        try {
            Food food = Kitchen.getFood(foodName);
            restaurant.setFood(food);
            restaurant.order(foodName);
        } catch (Exception e) {
            System.out.println("���ʧ�ܣ�" + e.getMessage());
        }
    }
}
