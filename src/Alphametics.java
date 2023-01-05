import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Alphametics {
    final Map<Character, Integer> values = new HashMap<>();
    Set<Integer> zeroNotAllowed = new HashSet<>();
    Set<Integer> characters = new HashSet<>();
    final int[] lhsFrequency = new int[26];
    final int[] rhsFrequency = new int[26];
    final int[] digits = new int[10];

    Alphametics(String equation) {
        String lhs = equation.split("==")[0];
        String rhs = equation.split("==")[1];
        parseInput(lhs, lhsFrequency);
        parseInput(rhs, rhsFrequency);
    }

    void parseInput(String input, int[] frequency) {
        for (String number : input.split("\\+")) {
            parseNumber(number, frequency);
        }
    }


    void parseNumber(String number, int[] frequency) {
        number = number.trim();
        int n = number.length();
        for (int i = 0; i < n; i++) {
            int index = number.charAt(i) - 'A';
            frequency[index] += Math.pow(10, n - i - 1);
            characters.add(index);
            if (i == 0 && n > 1) {
                zeroNotAllowed.add(index);
            }
        }
    }


    public Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        final int[] characters = this.characters.stream().mapToInt(Integer::intValue).toArray();
        Arrays.fill(digits, -1);
        if (solve(characters, 0)) {
            return values;
        }
        throw new UnsolvablePuzzleException();
    }

    private boolean solve(final int[] characters, int index) {
        if (index == characters.length) {
            return isEquationCorrect();
        }
        int character = characters[index];
        for (int i = 0; i < 10; i++) {
            int j = (i + index) % 10;
            if (j == 0 && zeroNotAllowed.contains(character)) continue;
            if (digits[j] != -1) continue;
            digits[j] = character;
            values.put((char) ('A' + character), j);
            if (solve(characters, index + 1)) {
                return true;
            }
            values.remove((char) ('A' + character));
            digits[j] = -1;
        }
        return false;
    }



    private boolean isEquationCorrect() {
        int lhs = 0, rhs = 0;
        for (int i = 0; i < 26; i++) {
            lhs += lhsFrequency[i] * values.getOrDefault((char) ('A' + i), 0);
            rhs += rhsFrequency[i] * values.getOrDefault((char) ('A' + i), 0);
        }
        return lhs == rhs && lhs != 0;
    }
}

