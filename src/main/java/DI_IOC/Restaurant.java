package DI_IOC;

public class Restaurant {



    private Food F;

    public void setFood(Food F){
        this.F = F;
    }

    public void order(String foodName){

        System.out.println("���˵���һ��"+foodName);

        System.out.println("�ϲˡ�������");

        F.eat();

    }

}