package com.mountainbranch.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// TODO Create copy of NeuralNetwork-object.
// TODO How to modify weight between generations (mutate etc)?
public class NeuralNetwork {
	private static final Random RANDOM = new Random();
	
	private final List<List<Neuron>> neuronLayers = new ArrayList<List<Neuron>>();
	
	public NeuralNetwork(int... neuronsPerLayer) {
		if (neuronsPerLayer.length < 2) {
			throw new IllegalArgumentException("Must have at least 2 layers");
		}
		for (int iLayer = 0; iLayer < neuronsPerLayer.length; iLayer++) {
			List<Neuron> newLayer = new ArrayList<Neuron>();
			List<Neuron> inputLayer = Collections.emptyList();
			if (iLayer > 0) {
				inputLayer = neuronLayers.get(iLayer-1);
			}
			while (newLayer.size() < neuronsPerLayer[iLayer]) {
				Neuron neuron = new Neuron();
				for (Neuron inputNeuron : inputLayer) {
					neuron.addInput(inputNeuron, RANDOM.nextGaussian()*0.1);
				}
				newLayer.add(neuron);
			}
			neuronLayers.add(newLayer);
		}
	}
	
	public void setInputs(double[] inputValues) {
		List<Neuron> inputLayer = neuronLayers.get(0);
		if (inputValues.length != inputLayer.size()) {
			throw new IllegalArgumentException("inputValues size is " + inputValues.length
					+ ", but this neural network has " + inputLayer.size() + " neurons.");
		}
		for (int i = 0; i < inputLayer.size(); i++) {
			inputLayer.get(i).setValue(inputValues[i]);
		}
	}
	
	public double[] getOutputs() {
		for (int i = 1; i < neuronLayers.size(); i++) {
			for (Neuron neuron : neuronLayers.get(i)) {
				neuron.updateValue();
			}
		}
		
		List<Neuron> outputLayer = neuronLayers.get(neuronLayers.size()-1);
		double[] outputs = new double[outputLayer.size()];
		for (int i = 0; i < outputLayer.size(); i++) {
			outputs[i] = outputLayer.get(i).getValue();
		}
		return outputs;
	}
}
