package com.mountainbranch.neuralnetwork;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Evolution {
	private static final Random RANDOM = new Random();
	
	private final double mutationStandardDeviation;
	
	public Evolution(double mutationStandardDeviation) {
		this.mutationStandardDeviation = mutationStandardDeviation;
	}
	
	public List<NeuralNetwork> generatePopulation(int size, int... neuronsPerLayer) {
		List<NeuralNetwork> population = new LinkedList<NeuralNetwork>();
		while (population.size() < size) {
			NeuralNetwork nn = new NeuralNetwork(neuronsPerLayer);
			nn.mutateAllWeights(mutationStandardDeviation);
			population.add(nn);
		}
		return population;
	}
	
	public List<NeuralNetwork> generateNextGeneration(List<NeuralNetwork> currentGeneration) {
		List<NeuralNetwork> nextGeneration = new LinkedList<NeuralNetwork>();
		final Map<NeuralNetwork, Integer> rank = new HashMap<NeuralNetwork, Integer>();
		final int targetSize = currentGeneration.size();
		
		// Remove the worst
		int numberToRemove = currentGeneration.size() / 4;
		while (currentGeneration.size() > targetSize - numberToRemove) {
			currentGeneration.remove(currentGeneration.size()-1);
		}
		
		// Give the most fit 5 times the weight
		for (int i = 1; i < 5; i++) {
			currentGeneration.add(0, currentGeneration.get(0));
		}
		
		int totalWeight = (currentGeneration.size()+1) * currentGeneration.size() / 2;
		while (nextGeneration.size() < targetSize) {
			int rand = RANDOM.nextInt(totalWeight);
			for (int i = 0; i < currentGeneration.size(); i++) {
				int weight = currentGeneration.size()-i;
				if (rand < weight) {
					NeuralNetwork nn = new NeuralNetwork(currentGeneration.get(i));
					nn.mutateAllWeights(mutationStandardDeviation);
					nextGeneration.add(nn);
					rank.put(nn, i);
					break;
				} else {
					rand -= weight;
				}
			}
		}
		
		Collections.sort(nextGeneration, new Comparator<NeuralNetwork>(){
			@Override
			public int compare(NeuralNetwork o1, NeuralNetwork o2) {
				return rank.get(o1) - rank.get(o2);
			}
		});
		
		return nextGeneration;
	}
}
