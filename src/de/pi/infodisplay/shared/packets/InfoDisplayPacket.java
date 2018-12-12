package de.pi.infodisplay.shared.packets;

public class InfoDisplayPacket {
	private Object o;
	
	public InfoDisplayPacket(Object o) {
		this.o = o;
	}
	
	public static InfoDisplayPacket fromString(String str) {
		String[] values = str.split("::");
		Object o;
		switch (values[0]) {
		case "Integer":
			o = new Integer(Integer.parseInt(values[1]));
			break;
			
		case "String":
			o = values[1];
			break;
			
		case "Boolean":
			o = new Boolean(Boolean.getBoolean(values[1]));
			break;

		default:
			o = null;
			break;
		}
		return new InfoDisplayPacket(o);
	}
	
	public String toString() {
		String out = "";
		
		if(this.o instanceof Integer) {
			out += "Integer";
		}
		else if(this.o instanceof String) {
			out += "String";
		}
		else if(this.o instanceof Boolean) {
			out += "Boolean";
		}
		else {
			out += "Object";
		}
		
		out += "::";
		
		out += this.o.toString();
		
		return out;
	}
}
