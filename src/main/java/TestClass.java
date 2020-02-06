public class TestClass {
    public static void main(String[] args) {
        String string = "blava vlavalsda                     ";

        string = string.replace("\"", "");

        System.out.println(string);
    }
}
