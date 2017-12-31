package com.mountainbranch.neuralnetwork;

import com.mountainbranch.cars.SimulationGameState;
import com.mountainbranch.gameframework.builder.GameBuilder;

public class Main {

	public static void main(String[] args) {
		new GameBuilder(new SimulationGameState(), 60, false, null).startGame();
		NeuralNetwork nn = new NeuralNetwork(5, 4, 3, 2);
		NeuralNetwork nn2 = new NeuralNetwork(nn);
		nn.mutateAllWeights(0.1);
		//printWeights(nn);
		//printOutputs(nn, 0.1, 0.2, 1.0, 1.0, 1.0);
	}
	
	public static void printWeights(NeuralNetwork nn) {
		int[] neuronsPerLayer = nn.getNeuronsPerLayer();
		for (int iLayer = 1; iLayer < neuronsPerLayer.length; iLayer++) {
			for (int iOutputNeuron = 0; iOutputNeuron < neuronsPerLayer[iLayer]; iOutputNeuron++) {
				for (int iInputNeuron = 0; iInputNeuron < neuronsPerLayer[iLayer-1]; iInputNeuron++) {
					System.out.println((iLayer-1) + ":" + iInputNeuron + "->"
							+ iLayer + ":" + iOutputNeuron + " "
							+ nn.getWeight(iLayer, iOutputNeuron, iInputNeuron));
				}
			}
		}
	}
	
	public static void printOutputs(NeuralNetwork nn, double... inputs) {
		nn.setInputs(inputs);
		double[] outputs = nn.getOutputs();
		for (int i = 0; i < outputs.length; i++) {
			System.out.println("Output[" + i + "]: " + outputs[i]);
		}
	}
}
