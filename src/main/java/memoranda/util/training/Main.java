package memoranda.util.training;

public class Main {
    public static void main(String[] args) {
        //Student student = new Student(743676084);
        //Student student2 = new Student(596206995);
        //Student.saveStudents();

        for(Integer stu3 : Student.studentList) {
            System.out.println(Member.lookupMember(stu3).getFullName());
        }
    }
}
