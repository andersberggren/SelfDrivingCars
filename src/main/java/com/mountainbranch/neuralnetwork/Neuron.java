package com.mountainbranch.neuralnetwork;

import java.util.LinkedList;
import java.util.List;

public class Neuron {
	private final List<NeuronLink> inputs  = new LinkedList<NeuronLink>();
	private double value = 0.0;

	/**
	 * Update the value of this {@code Neuron}, based on the value of input neurons.
	 */
	public void updateValue() {
		double inputValue = 0.0;
		for (NeuronLink link : inputs) {
			inputValue += link.getValue();
		}
		// Use sigmoid function and translate value from (-1,1) to (0,1)
		value = (Math.tanh(inputValue) + 1.0) / 2.0;
	}
	
	/**
	 * Explicitly sets the value of this {@code Neuron}.
	 * May only be called on a {@code Neuron} in the input layer (no inputs from other neurons).
	 * 
	 * @param newValue
	 */
	public void setValue(double newValue) {
		if (!inputs.isEmpty()) {
			throw new RuntimeException("Not allowed to call setValue on a "
					+ this.getClass() + " that has inputs");
		}
		value = newValue;
	}
	
	public double getValue() {
		return value;
	}
	
	public void addInput(Neuron neuron, double weight) {
		inputs.add(new NeuronLink(neuron, weight));		
	}
}
