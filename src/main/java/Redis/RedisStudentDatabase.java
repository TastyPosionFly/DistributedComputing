package Redis;
import redis.clients.jedis.Jedis;

public class RedisStudentDatabase {
    private Jedis jedis;

    public RedisStudentDatabase() {
        // 连接到本地的 Redis 服务
        this.jedis = new Jedis("localhost", 6379);
    }

    // 添加一个学生
    public void addStudent(String id, String name, String age, String grade) {
        jedis.hset("student:" + id, "id", id);
        jedis.hset("student:" + id, "name", name);
        jedis.hset("student:" + id, "age", age);
        jedis.hset("student:" + id, "grade", grade);
    }

    // 删除一个学生
    public void deleteStudent(String id) {
        jedis.del("student:" + id);
    }

    // 修改一个学生
    public void updateStudent(String id, String name, String age, String grade) {
        jedis.hset("student:" + id, "name", name);
        jedis.hset("student:" + id, "age", age);
        jedis.hset("student:" + id, "grade", grade);
    }

    // 根据id查询学生信息
    public void getStudentById(String id) {
        System.out.println(jedis.hgetAll("student:" + id));
    }

    // 查询所有学生的信息
    public void getAllStudents() {
        for (String key : jedis.keys("student:*")) {
            System.out.println(jedis.hgetAll(key));
        }
    }

    public static void main(String[] args) {
        RedisStudentDatabase database = new RedisStudentDatabase();

        // 添加学生
        database.addStudent("1", "Alice", "20", "A");
        database.addStudent("2", "Bob", "21", "B");
        database.addStudent("3", "Charlie", "22", "C");

        // 查询所有学生的信息
        database.getAllStudents();

        // 修改学生信息
        database.updateStudent("1", "Alice Smith", "21", "A+");

        // 查询指定学生的信息
        database.getStudentById("1");

        // 删除学生
        database.deleteStudent("3");

        // 再次查询所有学生的信息
        database.getAllStudents();

        // 关闭连接
        database.jedis.close();
    }
}
