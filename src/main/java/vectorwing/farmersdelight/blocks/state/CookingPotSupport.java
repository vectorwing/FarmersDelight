package vectorwing.farmersdelight.blocks.state;

import net.minecraft.util.IStringSerializable;

public enum CookingPotSupport implements IStringSerializable
{
	NONE("none"),
	TRAY("tray"),
	HANDLE("handle");

	private final String supportName;

	CookingPotSupport(String name) {
		this.supportName = name;
	}

	@Override
	public String toString() {
		return this.getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return this.supportName;
	}
}
