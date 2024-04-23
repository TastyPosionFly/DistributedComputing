package DI_IOC;

public interface Food {
    void eat();

}

class Beef implements Food {
    @Override

    public void eat() {

    // TODO Auto-generated method stub

        System.out.println("��ţ�š�����");
    }

}

class Salad implements Food {


    @Override

    public void eat() {

        // TODO Auto-generated method stub

        System.out.println("��ɳ��������");

    }

}