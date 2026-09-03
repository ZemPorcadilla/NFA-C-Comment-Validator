package NFA;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Ccomment {

   public Ccomment() {
   }

   public static boolean runNFA(String input) {

        // State 0 = Start
        // State 1 = Saw '/'
        // State 2 = Inside comment
        // State 3 = Saw '*' inside comment
        // State 4 = Accept

      HashSet<Integer> currentStates = new HashSet<>();
      currentStates.add(0);

      for (int i = 0; i < input.length(); i++) {

            char currentChar = input.charAt(i);

            // Alphabet:
            // '/' = slash
            // '*' = star
            // 'a' = any other character
            char symbol;

            if (currentChar != '*' && currentChar != '/') {
               symbol = 'a';
            } else {
               symbol = currentChar;
            }

            HashSet<Integer> nextStates = new HashSet<>();

            for (int state : currentStates) {

               switch (state) {

                    // State 0: Start
                  case 0:
                        if (symbol == '/') {
                           nextStates.add(1);
                        }
                        break;

                    // State 1: Saw '/'
                  case 1:
                        if (symbol == '*') {
                           nextStates.add(2);
                        }
                        break;

                    // State 2: Inside comment
                  case 2:
                        // Stay inside the comment
                        nextStates.add(2);

                        // '*' may start the closing sequence
                        if (symbol == '*') {
                           nextStates.add(3);
                        }
                        break;

                    // State 3: Saw '*'
                  case 3:
                        // '/' closes the comment
                        if (symbol == '/') {
                           nextStates.add(4);
                        }
                        break;

                    // State 4: Accepting state
                  case 4:
                        break;

                  default:
                        break;
               }
            }

            currentStates = nextStates;

            // No possible state = rejected
            if (currentStates.isEmpty()) {
               return false;
            }
      }

        // Accepted only if state 4 is reached
      return currentStates.contains(4);
   }

   public static void main(String[] args) {

      LinkedHashMap<String, String> testCases = new LinkedHashMap<>();

        // Accepted test cases
      testCases.put("/*a*/", "Expected: Accepted");
      testCases.put("/**/", "Expected: Accepted");
      testCases.put("/***/", "Expected: Accepted");
      testCases.put("/*aaa*aaa*/", "Expected: Accepted");
      testCases.put("/*a/a*/", "Expected: Accepted");

        // Rejected test cases
      testCases.put("/**", "Expected: Rejected");
      testCases.put("/**/a/**/", "Expected: Rejected");
      testCases.put("aaa/**/a", "Expected: Rejected");
      testCases.put("/*/", "Expected: Rejected");
      testCases.put("/**a/", "Expected: Rejected");
      testCases.put("//aaaa", "Expected: Rejected");

      System.out.println("==================================================");
      System.out.println("       NFA C-STYLE COMMENT VALIDATOR (JAVA)");
      System.out.println("==================================================\n");

      System.out.printf(
            "%-18s | %-10s | %s%n",
            "Input String",
            "Result",
            "Evaluation"
      );

      System.out.println("--------------------------------------------------");

      for (Map.Entry<String, String> entry : testCases.entrySet()) {

            boolean accepted = runNFA(entry.getKey());

            String result;

            if (accepted) {
               result = "ACCEPTED";
            } else {
               result = "REJECTED";
            }

            System.out.printf(
               "%-18s | %-10s | %s%n",
               entry.getKey(),
               result,
               entry.getValue()
            );
      }

      System.out.println("\n==================================================");

      Scanner scanner = new Scanner(System.in);

      System.out.print("Enter your own string to test: ");

      if (scanner.hasNextLine()) {

            String input = scanner.nextLine();

            boolean accepted = runNFA(input);

            System.out.println(
               "Input: \"" + input + "\" -> Status: "
               + (accepted ? "ACCEPTED" : "REJECTED")
            );
      }

      scanner.close();
   }
}