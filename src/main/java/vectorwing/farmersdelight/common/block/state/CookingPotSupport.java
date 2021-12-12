package vectorwing.farmersdelight.common.block.state;

import net.minecraft.util.StringRepresentable;

public enum CookingPotSupport implements StringRepresentable
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
