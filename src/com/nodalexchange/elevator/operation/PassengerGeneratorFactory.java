package com.nodalexchange.elevator.operation;

class PassengerGeneratorFactory {
	static PassengerGenerator buildPassengerGenerator(String generatorClassName) {
		PassengerGenerator returnValue = null;
		try {
			returnValue = (PassengerGenerator)Class.forName(generatorClassName).newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException("Error building a passenger generator", e);
		}
		
		return returnValue;
	}
}
