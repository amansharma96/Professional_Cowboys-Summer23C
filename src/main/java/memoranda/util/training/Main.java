package memoranda.util.training;

public class Main {
    public static void main(String[] args) {

        for(Student stu3 : Student.studentList.values()) {
            System.out.println(stu3.getFullName());
        }
    }
}
