package AOP;

import java.util.Scanner;

interface Vehicle{
    double tollFee(double distance);
}

class SmallCar implements Vehicle{
    @Override
    public double tollFee(double distance) {

        return distance*0.5;
    }
}

class Bus implements Vehicle{

    @Override
    public double tollFee(double distance) {
        System.out.println("请输入乘客数量：");
        Scanner scanner = new Scanner(System.in);
        int passenger = scanner.nextInt();
        if (passenger < 7){
            return distance*0.5;
        } else if (passenger >=7 && passenger <20){
            return distance*1;
        } else if(passenger >=20){
            return distance*1.2;
        } else {
            return -1;
        }
    }
}

class Truck implements Vehicle{
    @Override
    public double tollFee(double distance) {
        System.out.println("输入吨数");
        Scanner scanner = new Scanner(System.in);
        double weight = scanner.nextDouble();
        if (weight > 0){
            return distance * weight;
        } else {
            return -1;
        }
    }
}