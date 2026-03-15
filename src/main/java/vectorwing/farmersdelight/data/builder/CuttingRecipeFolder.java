package vectorwing.farmersdelight.data.builder;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CuttingRecipeFolder implements StringRepresentable
{
	CUTTING("cutting"),
	SALVAGING("salvaging");

	private final String folder;

	CuttingRecipeFolder(String recipeFolder) {
		this.folder = recipeFolder;
	}

	@Override
	public String toString() {
		return this.getSerializedName();
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.folder;
	}
}
