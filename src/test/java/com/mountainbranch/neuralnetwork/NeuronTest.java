package com.mountainbranch.neuralnetwork;

import static org.junit.Assert.*;

import org.junit.Test;

public class NeuronTest {
	private static final double DELTA = 0.000001;

	@Test
	public void setGetValue() {
		Neuron neuron = new Neuron();

		double value = 0.6;
		neuron.setValue(value);
		assertEquals(value, neuron.getValue(), DELTA);
		
		value = 0.1;
		neuron.setValue(value);
		assertEquals(value, neuron.getValue(), DELTA);
		
		// The neuron has no inputs. The sum of inputs is 0, and neuron value becomes 0.5
		neuron.updateValue();
		assertEquals(0.5, neuron.getValue(), DELTA);
		
		// Try to set value that is out-of-bounds
		try {
			neuron.setValue(1.1);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			// Expected path
		}
		
		// Try to set value that is out-of-bounds
		try {
			neuron.setValue(-0.1);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			// Expected path
		}
		
		neuron.addInput(new Neuron(), 1.0);
		try {
			neuron.setValue(0.5);
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
			// Expected path
		}
	}
	
	@Test
	public void weight() {
		// Neuron A and B are inputs to neuron C
		Neuron neuronA = new Neuron();
		Neuron neuronB = new Neuron();
		Neuron neuronC = new Neuron();
		double weightA = 0.2;
		double weightB = 2.7;
		neuronC.addInput(neuronA, weightA);
		neuronC.addInput(neuronB, weightB);
		
		assertEquals(weightA, neuronC.getWeight(0), DELTA);
		assertEquals(weightB, neuronC.getWeight(1), DELTA);
		
		try {
			neuronC.getWeight(-1);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			// Expected path
		}
		
		try {
			neuronC.getWeight(2);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			// Expected path
		}
	}
	
	@Test
	public void updateValue() {
		// Neuron A and B are inputs to neuron C
		Neuron neuronA = new Neuron();
		Neuron neuronB = new Neuron();
		Neuron neuronC = new Neuron();
		double weightA = 0.2;
		double weightB = 2.7;
		neuronC.addInput(neuronA, weightA);
		neuronC.addInput(neuronB, weightB);
		
		neuronA.setValue(0.2);
		neuronB.setValue(0.9);
		neuronC.updateValue();
		double expectedValue = (Math.tanh(0.2*0.2 + 2.7*0.9) + 1.0) / 2.0;
		assertEquals(expectedValue, neuronC.getValue(), DELTA);
	}
	
	@Test
	public void mutateWeights() {
		Neuron neuronA = new Neuron();
		Neuron neuronB = new Neuron();
		double weight = 0.2;
		neuronB.addInput(neuronA, weight);
		neuronB.mutateWeights(1.0);
		
		// Not completely deterministic
		assertNotEquals(weight, neuronB.getWeight(0), DELTA);
	}
}
