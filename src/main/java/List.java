import java.util.*;

public class List {
    static ArrayList<ArrayList<String>> lists = new ArrayList<>();
    static int numLists = 0;

    static HashMap<Integer, String> indexToName = new HashMap<>();
    static HashMap<String, Integer> nameToIndex = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();

        while (!input.equals("print")){
            int a = input.indexOf(":");
            int b = a + input.substring(a+1).indexOf(":") + 1;
            String name = input.substring(a + 1, b);

            String method = input.substring(0, a);
            if (method.equals("new") || !nameToIndex.containsKey(name)) {
                ArrayList<String> noName = new ArrayList<>();
                noName.add(input.substring(b + 1));
                lists.add(noName);

                indexToName.put(numLists, name);
                nameToIndex.put(name, numLists);
                numLists++;

            } else if (method.equals("add")) {
                int index = nameToIndex.get(name);
                lists.get(index).add(input.substring(b + 1));
            }

            input = in.nextLine();
        }

        for (int i = 0; i < numLists; i++) {
            System.out.println(indexToName.get(i) + ":" + lists.get(i));
        }

        in.close();
    }
}
