package util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Util {

    public boolean checker(List<Map<String, Object>> list) {
        for (int i = 1; i < list.size(); i++) {
            int currentId = (int) list.get(i).get("id");
            int previousID = (int) list.get(i - 1).get("id");
            if (currentId != previousID + 1) {
                return false;
            }
        }
        return true;
    }

    public String findUserByID(List<Map<String, Object>> list, int id) {
        for (int i = 1; i < list.size(); i++) {
            if ((int) list.get(i).get("id") == id) {
                return list.get(i).toString();
            }
        }
        return null;
    }


    public String findUserByID(String path) {

        try (Scanner scanner = new Scanner(Paths.get(path))) {
            return  scanner.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
return null;
    }
}
