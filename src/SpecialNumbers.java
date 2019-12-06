import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SpecialNumbers {

    private static final String alphabet = "ABEKMHOPCTYX";
    private static final List<String> CHARS = new ArrayList<>(Arrays.asList(alphabet.split("")));
    /**
     * regions from https://pddmaster.ru/avtomobili/kody-regionov-na-avtomobilnyx-nomerax.html#2
     */
    private static final List<String> REGIONS = new ArrayList<>(Arrays.asList(("01, 02, 102, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 113, 14, 15, 16, " +
            "116, 716, 17, 18, 19, 21, 121, 22, 23, 93, 123, 24, 84, 88, 124, 25, 125, 26, 126, 27, 28, 29, 30, " +
            "31, 32, 33, 34, 134, 35, 36, 136, 37, 38, 85, 138, 39, 91, 40, 41, 42, 142, 43, 44, 45, 46, 47, 147, 48, 49, " +
            "50, 90, 150, 190, 750, 51, 52, 152, 53, 54, 154, 55, 56, 57, 58, 59, 81").split(", ")));

    static {
        REGIONS.sort(Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        SpecialNumbers specialNumbers = new SpecialNumbers();
        //создание всех возможных автомобильных номеров
        Map<String, Map<String, List<String>>> allAutoSigns = specialNumbers.generateSigns();
        //создание "блатных" номеров
        ArrayList<String> allAutoSpecialSigns = specialNumbers.generateSpecialSigns();


        HashSet<String> allAutoSpecialSignsHashSet = new HashSet<>(allAutoSpecialSigns);
        TreeSet<String> allAutoSpecialSignsTreeSet = new TreeSet<>(allAutoSpecialSigns);
        ArrayList<String> allAutoSpecialSignsSortedArrayList = new ArrayList<>(allAutoSpecialSigns);
        Collections.sort(allAutoSpecialSignsSortedArrayList);

        Scanner in = new Scanner(System.in);

        while (true){
            if(in.nextLine().equals("EXIT")){
                break;
            }
            else {
                System.out.println("Введите в консоль номер автомобиля.");
                System.out.println("Enter region: ");
                String region = in.nextLine();
                System.out.println("Enter characters(Используйте заглавные буквы латиницы): ");
                String characters = in.nextLine();
                System.out.println("Enter numbers(Три цифры): ");
                String numbers = in.nextLine();

                System.out.println("You enter auto sign: " + characters.charAt(0) + numbers + characters.substring(1, 3) + region);
                System.out.println("Start searching. Please, wait...");

                //Проверка номера на корректность
                Map<String, List<String>> regionAutoSigns = allAutoSigns.get(region);
                if (regionAutoSigns != null) {
                    List<String> integersForCharacters = regionAutoSigns.get(characters);
                    if (integersForCharacters != null) {
                        if (integersForCharacters.indexOf(numbers) != -1) {
                            System.out.println("Number correct.");
                        } else {
                            System.out.println("Numbers not found.");
                        }
                    } else {
                        System.out.println("Characters not found.");
                    }
                } else {
                    System.out.println("Region not found.");
                }

                long beforeArray = System.nanoTime();
                for (String string : allAutoSpecialSigns) {
                    if (string.contains(numbers + characters + region)) {
                        System.out.println("Этот номер блатной: " + string);
                        System.out.println("Время поиска ArrayList: " + (System.nanoTime() - beforeArray) + "ms");
                    }
                }
                long beforeHashSet = System.nanoTime();
                for (String string : allAutoSpecialSignsHashSet) {
                    if (string.contains(numbers + characters + region)) {
                        System.out.println("Время поиска HashSet: " + (System.nanoTime() - beforeHashSet) + "ms");
                    }
                }
                long beforeTreeSet = System.nanoTime();
                for (String string : allAutoSpecialSignsTreeSet) {
                    if (string.contains(numbers + characters + region)) {
                        System.out.println("Время поиска TreeSet: " + (System.nanoTime() - beforeTreeSet) + "ms");
                    }
                }
                long beforeSortedArray = System.nanoTime();
                for (String string : allAutoSpecialSignsSortedArrayList) {
                    if (string.contains(numbers + characters + region)) {
                        System.out.println("Время поиска sorted ArrayList: " + (System.nanoTime() - beforeSortedArray) + "ms");
                    }
                }
                long beforeBinarySearch = System.nanoTime();
                int index = Collections.binarySearch(allAutoSpecialSignsSortedArrayList, (numbers + characters + region));

                if (index >= 0) {
                    System.out.println("Время поиска sorted ArrayList by binary Search: " + (System.nanoTime() - beforeBinarySearch) + "ms");
                } else if (index < -1) {
                    System.out.println("Номер не блатной. Время поиска: " + (System.nanoTime() - beforeBinarySearch) + "ms");
                }
            }
        }
        in.close();
    }

    //Генерация любых номеров
    private Map<String, Map<String, List<String>>> generateSigns() {
        List<String> allCharactersList = new ArrayList<>();
        for (String firstChar : CHARS) {
            for (String secondChar : CHARS) {
                for (String thirdChar : CHARS) {
                    allCharactersList.add(firstChar + secondChar + thirdChar);
                }
            }
        }

        List<String> allIntegersList = new ArrayList<>();
        for (Integer firstChar = 0; firstChar < 10; firstChar++) {
            for (Integer secondChar = 0; secondChar < 10; secondChar++) {
                for (Integer thirdChar = 0; thirdChar < 10; thirdChar++) {
                    allIntegersList.add(firstChar.toString() + secondChar.toString() + thirdChar.toString());
                }
            }
        }

        Map<String, Map<String, List<String>>> allAutoSigns = new TreeMap<>();

        Map<String, List<String>> charsToIntegers = new TreeMap<>();
        for (String chars : allCharactersList) {
            charsToIntegers.put(chars, allIntegersList);
        }

        for (String region : REGIONS) {
            allAutoSigns.put(region, charsToIntegers);
        }

        return allAutoSigns;
    }

    private ArrayList<String> generateSpecialSigns(){
        //Создаём список "блатных" номеров
        //Три одинаковых буквы
        ArrayList<String> threeLetters = new ArrayList<>();
        for(String y : CHARS)
        { threeLetters.add(y + y + y); }
        //Блатные цифры
        ArrayList<Integer> threeNumbers = new ArrayList<>();
        for(int i = 1; i < 10; i++)
        { threeNumbers.add(i*100 + i*10 + i); }
        ArrayList<String> stringNumbers = new ArrayList<>();
        for(Integer newInt : threeNumbers)
        { stringNumbers.add(newInt.toString()); }
        stringNumbers.add(0, "000");
        stringNumbers.add("001");
        stringNumbers.add("002");
        stringNumbers.add("003");
        stringNumbers.add("004");
        stringNumbers.add("005");
        stringNumbers.add("006");
        stringNumbers.add("007");
        stringNumbers.add("008");
        stringNumbers.add("009");
        stringNumbers.add("123");
        stringNumbers.add("404");

        List<String> allCharactersList = new ArrayList<>();
        for (String firstChar : CHARS) {
            for (String secondChar : CHARS) {
                for (String thirdChar : CHARS) {
                    allCharactersList.add(firstChar + secondChar + thirdChar);
                }
            }
        }
        //Генерация возможных "блатных" номеров
        ArrayList<String> charsAndNumbers = new ArrayList<>();
        for(String threeChars : threeLetters)
        {
            for(int i = 10; i <= 99; i++)
            {
                charsAndNumbers.add("0" + i + threeChars);
            }
        }
        for(String threeChars : threeLetters)
        {
            for(int i = 100; i <= 999; i++)
            {
                charsAndNumbers.add(i + threeChars);
            }
        }
        ArrayList<String> allAutoSpecialSigns = new ArrayList<>();
        for(String x : charsAndNumbers)
        {
            for(String y : REGIONS)
            {
                allAutoSpecialSigns.add(x + y);
            }
        }
        for(String o : stringNumbers)
        {
            for(String i : allCharactersList)
            {
                for(String u : REGIONS)
                {
                    allAutoSpecialSigns.add(o + i + u);
                }
            }
        }
        return allAutoSpecialSigns;
    }
}