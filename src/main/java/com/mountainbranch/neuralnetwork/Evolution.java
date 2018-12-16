package com.mountainbranch.neuralnetwork;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Evolution {
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
		
		// Best individual is automatically qualified for next generation (without mutation)
		NeuralNetwork bestNN = new NeuralNetwork(currentGeneration.get(0));
		nextGeneration.add(bestNN);
		rank.put(bestNN, -1);
		
		for (int iMax = 1; nextGeneration.size() < targetSize; iMax++) {
			for (int i = 0; i < iMax && nextGeneration.size() < targetSize; i++) {
				NeuralNetwork nn = new NeuralNetwork(currentGeneration.get(i));
				nn.mutateAllWeights(mutationStandardDeviation);
				nextGeneration.add(nn);
				rank.put(nn, i);
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
