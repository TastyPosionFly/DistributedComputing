package DI_IOC;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Kitchen {
    public static Food getFood(String foodName) {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new FileInputStream("D:/Work/DC/DistributedComputing/src/DI_IOC/food.xml"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        String food = (String) properties.get(foodName);

        try {
            Class<? extends Food> clazz = (Class<? extends Food>) Class.forName("DI_IOC."+food);
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
