package vectorwing.farmersdelight.data;

import com.google.common.hash.Hashing;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TODO 26.2: Replace this bridge with real providers for generated resources, especially the client
 * ModelProvider port for blockstates, item definitions, and models.
 */
public class ExistingGeneratedResources implements DataProvider
{
	private final Path generatedRoot;

	public ExistingGeneratedResources(PackOutput output) {
		this.generatedRoot = output.getOutputFolder();
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		if (!Files.exists(this.generatedRoot)) {
			return CompletableFuture.completedFuture(null);
		}

		List<GeneratedResource> resources;
		try (var paths = Files.walk(this.generatedRoot)) {
			resources = paths
					.filter(Files::isRegularFile)
					.map(ExistingGeneratedResources::readResource)
					.toList();
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to read existing generated resources", exception);
		}

		return CompletableFuture.allOf(resources.stream()
				.map(resource -> CompletableFuture.runAsync(() -> writeResource(cache, resource)))
				.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Existing Generated Resources";
	}

	private static GeneratedResource readResource(Path path) {
		try {
			return new GeneratedResource(path, Files.readAllBytes(path));
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to read " + path, exception);
		}
	}

	private static void writeResource(CachedOutput cache, GeneratedResource resource) {
		try {
			cache.writeIfNeeded(resource.path(), resource.contents(), Hashing.sha1().hashBytes(resource.contents()));
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to write " + resource.path(), exception);
		}
	}

	private record GeneratedResource(Path path, byte[] contents) {
	}
}
