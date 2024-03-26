package org.example;

import java.util.*;
import java.util.stream.IntStream;

public class TravelingSalesman {

    private final int[][] distances;
    private final int populationSize;
    private final int numCities;
    private List<int[]> population;

    private final int seed = 12345;

    private final Random generator = new Random();

    TravelingSalesman(int[][] distances, int population){
        this.distances = distances;
        this.populationSize = population;
        this.numCities = distances.length;
        generator.setSeed(seed);
    }

    public int solve(int generations){
        this.initializePopulation();

        for(int i = 0; i < generations; i++){
            List<int[]> newPopulation = new ArrayList<>();

            while (newPopulation.size() < this.populationSize){
                int[] a = selection();
                int[] b = selection();
                int[] c = crossover(a, b);
                mutate(c);
                newPopulation.add(c);
            }

            this.population = newPopulation;
        }

        int[] best_value =  getBestValue();
        int result = 0;

        for(int i = 0; i < best_value.length -1 ; i++)
            result += distances[best_value[i]][best_value[i+1]];

        return result + distances[distances.length - 1][0];

    }

    private void initializePopulation(){
        population = new ArrayList<>();
        for(int i = 0; i < populationSize; i++){
            population.add(generateRandomIndividual());
        }
    }

    private int[] generateRandomIndividual() {
        List<Integer> cities = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            cities.add(i);
        }
        Collections.shuffle(cities);
        return cities.stream().mapToInt(Integer::intValue).toArray();
    }

    private int evaluate(int[] actual){
        int distance = 0;

        for(int i = 0; i < this.numCities - 1; i++)
            distance += this.distances[actual[i]][actual[i+1]];

        return distance + distances[actual[this.numCities - 1]][actual[0]];
    }

    private void mutate(int[] actual){
        if(generator.nextDouble() < 0.1){
            int p = generator.nextInt(this.numCities);
            int r = generator.nextInt(this.numCities);
            int tmp = actual[p];

            actual[p] = actual[r];
            actual[r] = tmp;
        }
    }

    private int[] crossover(int[] A, int[] B){
        int[] offspring = new int[this.numCities];
        int start = generator.nextInt(numCities);
        int stop = generator.nextInt(numCities);

        for(int i = 0; i < this.numCities; i++){
            if(start < stop && i >= start && i <= stop)
                offspring[i] = A[i];

            if(start > stop && !(i <= start && i >= stop))
                offspring[i] = A[i];
        }

        for(int i = 0; i < this.numCities; i++){
            if (!containsCity(offspring, B[i])){
                for(int j = 0; j < this.numCities; j++){
                    if(offspring[j] == 0){
                        offspring[j] = B[i];
                        break;
                    }
                }
            }
        }

        return offspring;
    }

    private boolean containsCity(int[] array, int city) {
        for (int j : array)
            if (j == city)
                return true;

        return false;
    }

    private int[] selection(){
        int size = 5;
        int [] best = null;
        int bestDistance = Integer.MAX_VALUE;

        for(int i = 0; i < size; i++){
            int[] x = population.get(generator.nextInt(this.populationSize));
            int distance = this.evaluate(x);

            if(distance < bestDistance){
                bestDistance = distance;
                best = x;
            }
        }

        return best;
    }

    private int[] getBestValue(){
        int[] best = population.get(0);
        int bestDistance = evaluate(best);

        for (int[] x : population) {
            int distance = evaluate(x);

            if (distance < bestDistance) {
                bestDistance = distance;
                best = x;
            }
        }
        return best;
    }

}
