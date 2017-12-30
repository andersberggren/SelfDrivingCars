package com.mountainbranch.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {
	private static final Random RANDOM = new Random();
	private static final double MUTATION_STANDARD_DEVIATION = 0.1;
	
	private final List<List<Neuron>> neuronLayers = new ArrayList<List<Neuron>>();
	
	public NeuralNetwork(NeuralNetwork parent) {
		this(parent, parent.getNeuronsPerLayer());
	}
	
	public NeuralNetwork(int... neuronsPerLayer) {
		this(null, neuronsPerLayer);
	}
	
	private NeuralNetwork(NeuralNetwork parent, int[] neuronsPerLayer) {
		if (neuronsPerLayer.length < 2) {
			throw new IllegalArgumentException("Must have at least 2 layers");
		}
		for (int iLayer = 0; iLayer < neuronsPerLayer.length; iLayer++) {
			List<Neuron> newLayer = new ArrayList<Neuron>();
			List<Neuron> inputLayer = Collections.emptyList();
			if (iLayer > 0) {
				inputLayer = neuronLayers.get(iLayer-1);
			}
			for (int iOutputNeuron = 0; iOutputNeuron < neuronsPerLayer[iLayer]; iOutputNeuron++) {
				Neuron neuron = new Neuron();
				for (int iInputNeuron = 0; iLayer > 0 && iInputNeuron < neuronsPerLayer[iLayer-1];
						iInputNeuron++) {
					double baseWeight = 0.0;
					if (parent != null) {
						baseWeight = parent.getWeight(iLayer, iOutputNeuron, iInputNeuron);
					}
					double mutation = RANDOM.nextGaussian() * MUTATION_STANDARD_DEVIATION;
					neuron.addInput(inputLayer.get(iInputNeuron), baseWeight + mutation);
				}
				newLayer.add(neuron);
			}
			neuronLayers.add(newLayer);
		}
	}
	
	public void setInputs(double[] inputValues) {
		// Set value of neurons in input layer
		List<Neuron> inputLayer = neuronLayers.get(0);
		if (inputValues.length != inputLayer.size()) {
			throw new IllegalArgumentException("inputValues size is " + inputValues.length
					+ ", but this neural network has " + inputLayer.size() + " input neurons.");
		}
		for (int i = 0; i < inputLayer.size(); i++) {
			inputLayer.get(i).setValue(inputValues[i]);
		}
		
		// Propagate values through the rest of the layers
		for (int i = 1; i < neuronLayers.size(); i++) {
			for (Neuron neuron : neuronLayers.get(i)) {
				neuron.updateValue();
			}
		}
	}
	
	public double[] getOutputs() {
		List<Neuron> outputLayer = neuronLayers.get(neuronLayers.size()-1);
		double[] outputs = new double[outputLayer.size()];
		for (int i = 0; i < outputLayer.size(); i++) {
			outputs[i] = outputLayer.get(i).getValue();
		}
		return outputs;
	}
	
	public int[] getNeuronsPerLayer() {
		int[] neuronsPerLayer = new int[neuronLayers.size()];
		for (int i = 0; i < neuronLayers.size(); i++) {
			neuronsPerLayer[i] = neuronLayers.get(i).size();
		}
		return neuronsPerLayer;
	}
	
	public double getWeight(int layerIndexOfOutputNeuron, int indexOfOutputNeuron,
			int indexOfInputNeuron) {
		if (layerIndexOfOutputNeuron < 0 || layerIndexOfOutputNeuron >= neuronLayers.size()) {
			throw new IllegalArgumentException("Illegal layer: " + layerIndexOfOutputNeuron);
		}
		List<Neuron> layer = neuronLayers.get(layerIndexOfOutputNeuron);
		if (indexOfOutputNeuron < 0 || indexOfOutputNeuron >= layer.size()) {
			throw new IllegalArgumentException("Illegal indexOfOutputNeuron: " + indexOfOutputNeuron);
		}
		Neuron outputNeuron = layer.get(indexOfOutputNeuron);
		return outputNeuron.getWeight(indexOfInputNeuron);
	}
}
