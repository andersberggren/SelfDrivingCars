package com.mountainbranch.neuralnetwork;

public class Main {

	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork(5, 4, 3, 2);
		NeuralNetwork nn2 = new NeuralNetwork(nn);
		NeuralNetwork nn3 = new NeuralNetwork(nn2);
		printWeights(nn);
		printWeights(nn2);
		printWeights(nn3);
		
		printOutputs(nn, 0.1, 0.2, 1.0, 1.0, 1.0);
		printOutputs(nn, 1.0, 1.0, 1.0, 0.1, 0.2);
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
