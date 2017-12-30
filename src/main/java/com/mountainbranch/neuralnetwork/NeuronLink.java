package com.mountainbranch.neuralnetwork;

public class NeuronLink {
	private final Neuron from;
	private final double weight;
	
	public NeuronLink(Neuron from, double weight) {
		this.from = from;
		this.weight = weight;
	}
	
	public double getValue() {
		return from.getValue() * weight;
	}
	
	public double getWeight() {
		return weight;
	}
}
