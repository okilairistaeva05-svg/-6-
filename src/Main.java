import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface ShippingStrategy {
    double calculateShippingCost(double weight, double distance);
}

interface OrderObserver {
    void update(String orderInfo);
}

interface OrderSubject {
    void registerObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyObservers(String orderInfo);
}

class StandardShippingStrategy implements ShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return weight * 0.5 + distance * 0.1;
    }
}

class ExpressShippingStrategy implements ShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return (weight * 0.75 + distance * 0.2) + 10;
    }
}

class InternationalShippingStrategy implements ShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return weight * 1.0 + distance * 0.5 + 15;
    }
}

class NightShippingStrategy implements ShippingStrategy {
    public double calculateShippingCost(double weight, double distance) {
        return weight * 0.6 + distance * 0.15 + 20;
    }
}

class DeliveryContext {
    private ShippingStrategy strategy;
    public void setShippingStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }
    public double calculateCost(double weight, double distance) {
        if (strategy == null) throw new IllegalStateException("Стратегия доставки не установлена.");
        if (weight < 0 || distance < 0) throw new IllegalArgumentException("Вес и расстояние должны быть положительными числами.");
        return strategy.calculateShippingCost(weight, distance);
    }
}

class OrderNotifier implements OrderSubject {
    private List<OrderObserver> observers = new ArrayList<>();
    public void registerObserver(OrderObserver observer) { observers.add(observer); }
    public void removeObserver(OrderObserver observer) { observers.remove(observer); }
    public void notifyObservers(String orderInfo) {
        for (OrderObserver observer : observers) observer.update(orderInfo);
    }
}

class MobileAppObserver implements OrderObserver {
    public void update(String orderInfo) { System.out.println("[Мобильное приложение] " + orderInfo); }
}

class EmailObserver implements OrderObserver {
    public void update(String orderInfo) { System.out.println("[Email уведомление] " + orderInfo); }
}

public class Main {
    public static void main(String[] args) {
        DeliveryContext deliveryContext = new DeliveryContext();
        OrderNotifier notifier = new OrderNotifier();
        notifier.registerObserver(new MobileAppObserver());
        notifier.registerObserver(new EmailObserver());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите тип доставки: 1-Стандарт, 2-Экспресс, 3-Международная, 4-Ночная");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1": deliveryContext.setShippingStrategy(new StandardShippingStrategy()); break;
            case "2": deliveryContext.setShippingStrategy(new ExpressShippingStrategy()); break;
            case "3": deliveryContext.setShippingStrategy(new InternationalShippingStrategy()); break;
            case "4": deliveryContext.setShippingStrategy(new NightShippingStrategy()); break;
            default: System.out.println("Неверный выбор"); return;
        }
        System.out.println("Введите вес посылки (кг):");
        double weight = scanner.nextDouble();
        System.out.println("Введите расстояние доставки (км):");
        double distance = scanner.nextDouble();
        double cost = deliveryContext.calculateCost(weight, distance);
        String orderInfo = "Новый заказ: вес " + weight + " кг, расстояние " + distance + " км, стоимость " + cost;
        notifier.notifyObservers(orderInfo);
    }
}
