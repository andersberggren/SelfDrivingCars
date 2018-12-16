package com.mountainbranch.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork {
	private final List<List<Neuron>> neuronLayers = new ArrayList<List<Neuron>>();
	
	/**
	 * Creates a {@link NeuralNetwork} with the same number of neurons and
	 * weights as {@code parent}.
	 * 
	 * @param parent
	 */
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
					double weight = 0.0;
					if (parent != null) {
						weight = parent.getWeight(iLayer, iOutputNeuron, iInputNeuron);
					}
					neuron.addInput(inputLayer.get(iInputNeuron), weight);
				}
				// Bias node
				if (iLayer > 0) {
					Neuron biasNeuron = new Neuron();
					biasNeuron.setValue(1.0);
					double weight = 0.0;
					if (parent != null) {
						weight = parent.getWeight(iLayer, iOutputNeuron, neuronsPerLayer[iLayer-1]);
					}
					neuron.addInput(biasNeuron, weight);
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
	
	public void mutateAllWeights(double standardDeviation) {
		for (List<Neuron> neuronLayer : neuronLayers) {
			for (Neuron neuron : neuronLayer) {
				neuron.mutateWeights(standardDeviation);
			}
		}
	}
}
