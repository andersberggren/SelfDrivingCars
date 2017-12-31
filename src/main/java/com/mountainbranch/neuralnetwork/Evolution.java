package com.mountainbranch.neuralnetwork;

import java.util.LinkedList;
import java.util.List;
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
		int totalWeight = (currentGeneration.size()+1) * currentGeneration.size() / 2;
		
		while (nextGeneration.size() < currentGeneration.size()) {
			int rand = RANDOM.nextInt(totalWeight);
			for (int i = 0; i < currentGeneration.size(); i++) {
				int weight = currentGeneration.size()-i;
				if (rand < weight) {
					NeuralNetwork nn = new NeuralNetwork(currentGeneration.get(i));
					nn.mutateAllWeights(mutationStandardDeviation);
					nextGeneration.add(nn);
					break;
				} else {
					rand -= weight;
				}
			}
		}
		
		return nextGeneration;
	}
}
