package com.mountainbranch.neuralnetwork;

public class Main {

	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork(5, 4, 3, 2);
		printOutputs(nn, 0.1, 0.2, 1.0, 1.0, 1.0);
		printOutputs(nn, 1.0, 1.0, 1.0, 0.1, 0.2);
	}
	
	public static void printOutputs(NeuralNetwork nn, double... inputs) {
		nn.setInputs(inputs);
		double[] outputs = nn.getOutputs();
		for (int i = 0; i < outputs.length; i++) {
			System.out.println("Output[" + i + "]: " + outputs[i]);
		}
	}
}
