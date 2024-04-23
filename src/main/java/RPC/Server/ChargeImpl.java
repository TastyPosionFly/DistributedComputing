package RPC.Server;

import RPC.Charge;
import RPC.message;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChargeImpl implements Charge {
    private Map<String, String> userData;

    public ChargeImpl(){
        this.userData = new ConcurrentHashMap<>();
        loadUserDataFromFile();
    }

    @Override
    public RPC.message charge(String data) {
        StringBuilder response = new StringBuilder();

        if (data != null && data.length() == 20){
            if (data.startsWith("55746", 15)){
                String serverType = data.substring(0,1);
                String cardNumber = data.substring(1,10);
                String money = data.substring(10,15);
                System.out.println("users: " + cardNumber + " connected");
                if (!userData.containsKey(cardNumber)){
                    if (serverType.equals("0")){
                        response.append("User is not exist");
                    } else {
                        userData.put(cardNumber, money);
                        response.append("User created success, recharge success, recharge ").append(money).append(" yuan");
                    }
                } else {
                    if(serverType.equals("0")){
                        int originMoney = Integer.parseInt(userData.get(cardNumber));
                        int subMoney = Integer.parseInt(money);
                        if (originMoney < subMoney){
                            response.append("Money is not enough");
                        } else {
                            userData.put(cardNumber, String.valueOf(originMoney - subMoney));
                            response.append("Pay ").append(money).append(" success");
                        }
                    } else if (serverType.equals("1")) {
                        int originMoney = Integer.parseInt(userData.get(cardNumber));
                        int addMoney = Integer.parseInt(money);
                        userData.put(cardNumber, String.valueOf(originMoney + addMoney));
                        response.append("Recharge ").append(money).append(" success");
                    }
                }
            }else {
                response.append("Data illegal");
            }
        }else {
            response.append("Data Error");
        }
        response.append("\nTrade ").append(data).append(" finished");
        saveUserDataToFile();
        System.out.println(response.toString());
        message responseMessage = new message(response.toString());
        return responseMessage;
    }

    private void loadUserDataFromFile() {
        File file = new File("src//main//java//RPC//Server//save.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    userData.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void saveUserDataToFile() {
        File file = new File("src//main//java//RPC//Server//save.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                writer.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
