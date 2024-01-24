import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CengTreeParser
{
    public static ArrayList<CengBook> parseBooksFromFile(String filename)
    {
        ArrayList<CengBook> bookList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\|");
                bookList.add(new CengBook(
                        Integer.parseInt(split[0]),
                        split[1],
                        split[2],
                        split[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public static void startParsingCommandLine() throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] split = input.split("\\|");

            if (split[0].equals("quit")) {
                scanner.close();
                break;
            } else if (split[0].equals("print")) {
                CengBookRunner.printTree();
            } else if (split[0].equals("add")) {
                CengBookRunner.addBook(new CengBook(
                        Integer.parseInt(split[1]),
                        split[2],
                        split[3],
                        split[4]));
            } else if (split[0].equals("search")) {
                CengBookRunner.searchBook(Integer.parseInt(split[1]));
            } else {
                scanner.close();
                throw new IllegalStateException();
            }
        }
    }
}
