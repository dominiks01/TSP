package org.example;

import java.io.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {

        URL resourceUrl = Main.class.getResource("/input.txt");

        // Initialize delimiters
        String array[] = new String[] {
                "Białystok", "Olsztyn", "Warszawa", "Lublin", "Bydgoszcz", "Gdańsk",
                "Łódź", "Poznań", "Szczecin", "Gorzów Wielkopolski", "Wrocław",
                "Opole", "Katowice", "Kielce", "Kraków", "Rzeszów"
        };

        String everything;

        // Read file
        try (BufferedReader br = new BufferedReader(new FileReader(resourceUrl.getFile()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Map City -> Int to make distance matrix
        for(int i = 0; i < array.length; i++)
            everything = everything.replaceAll(array[i], String.valueOf(i));

        // Create distances vector
        int[][] distances = new int[array.length][array.length];

        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array.length; j++)
                distances[i][j] = 0;

        // parse string "{city_01} {city_02} {distance}" and create matrix
        for(String line: everything.split("\n")){
            String[] parts = line.split("\\s+");

            distances[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])]  = Integer.parseInt(parts[2]);
            distances[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])]  = Integer.parseInt(parts[2]);
        }

        TravelingSalesmanDynamic tspGA = new TravelingSalesmanDynamic(distances);
        System.out.println(tspGA.solve());

//        TravelingSalesman tspGa = new TravelingSalesman(distances, 1000);
//        System.out.println(tspGa.solve(1000));
    }
}