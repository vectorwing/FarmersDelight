package vectorwing.farmersdelight.data.builder;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

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
	public @NonNull String getSerializedName() {
		return this.folder;
	}
}


