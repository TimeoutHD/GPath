package de.pi.infodisplay.shared.packets;

import java.io.IOException;
import java.util.UUID;

import de.pi.infodisplay.shared.security.AuthentificationKey;
import io.netty.buffer.ByteBuf;

public class PacketServerOutAuthorizeUser extends Packet {
	
	private UUID userID;
	private boolean loginResult;
	private AuthentificationKey securityKey;

	private PacketServerOutAuthorizeUser() {
		super(101);
	}
	
	public PacketServerOutAuthorizeUser(UUID userID, boolean loginResult) {
		this();
		this.userID = userID;
		this.loginResult = loginResult;
		this.securityKey = new AuthentificationKey();
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.userID = UUID.fromString(Packet.decodeString(byteBuf));
		this.loginResult = byteBuf.readBoolean();
		this.securityKey = new AuthentificationKey(Packet.decodeString(byteBuf));
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		Packet.encodeString(byteBuf, userID.toString());
		byteBuf.writeBoolean(loginResult);
		Packet.encodeString(byteBuf, securityKey.getKeySet());
	}
	
	public AuthentificationKey getSecurityKey() {
		return securityKey;
	}
	
	public boolean getLoginResult() {
		return loginResult;
	}
	
	public UUID getUserID() {
		return userID;
	}
}
