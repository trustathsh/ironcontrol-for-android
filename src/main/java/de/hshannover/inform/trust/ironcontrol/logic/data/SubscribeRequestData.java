package de.hshannover.inform.trust.ironcontrol.logic.data;

/**
 * Class for connection management
 * @author Daniel Wolf
 * @author Anton Saenko
 * @author Arne Loth
 * @since 0.5
 */

public class SubscribeRequestData extends SearchRequestData{

	private Operation type;

	public SubscribeRequestData(Operation type){
		this.type = type;
	}

	public SubscribeRequestData(){

	}

	public Operation getType() {
		return type;
	}

	public void setType(Operation type) {
		this.type = type;
	}

}