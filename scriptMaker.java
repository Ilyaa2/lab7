import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class scriptMaker {
    public static void main(String[] args) throws IOException {
        String prefix = "add\n"+ "P3110";
        String s = "6\n" +
                "7\n" +
                "23\n" +
                "20\n" +
                "DISTANCE_EDUCATION\n" +
                "FIRST\n" +
                "Саша\n" +
                "2003\n" +
                "JANUARY\n" +
                "21\n" +
                "11\n" +
                "11\n" +
                "11\n" +
                "182\n" ;
        String postfix =  "ORANGE";
        for (int i = 0; i < 500; i++) {


            try (FileWriter writer = new FileWriter("src/script3.txt", true)) {
                // запись всей строки
                writer.write(prefix+String.valueOf(i)+"\n"+s+randomString()+"\n"+postfix+"\n");
                writer.flush();
            }
        }


    }

    public static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        //System.out.println(generatedString);
        return buffer.toString();
    }

}
