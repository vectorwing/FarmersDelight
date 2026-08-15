package vectorwing.farmersdelight;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceIntegrityTest
{
	private static final Path ASSET_ROOT = Path.of("src", "main", "resources", "assets", "farmersdelight");
	private static final Pattern TEXTURE_ENTRY = Pattern.compile("\\\"[^\\\"]+\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

	@Test
	void textureDirectoryContainsPngAssets() throws IOException {
		Path textureDirectory = ASSET_ROOT.resolve("textures");
		assertTrue(Files.isDirectory(textureDirectory), "Missing Farmer's Delight texture directory");

		try (Stream<Path> files = Files.walk(textureDirectory)) {
			assertTrue(files.anyMatch(path -> path.toString().endsWith(".png")), "No Farmer's Delight texture PNGs were found");
		}
	}

	@Test
	void localModelTexturesExist() throws IOException {
		List<String> missingTextures = new ArrayList<>();
		Path modelDirectory = ASSET_ROOT.resolve("models");

		try (Stream<Path> files = Files.walk(modelDirectory)) {
			for (Path model : files.filter(path -> path.toString().endsWith(".json")).toList()) {
				for (String texture : textureReferences(Files.readString(model))) {
					if (!texture.startsWith("#") && isLocalTexture(texture)) {
						Path texturePath = ASSET_ROOT.resolve("textures").resolve(texturePath(texture) + ".png");
						if (!Files.isRegularFile(texturePath)) {
							missingTextures.add(ASSET_ROOT.relativize(model) + " -> " + texture);
						}
					}
				}
			}
		}

		assertTrue(missingTextures.isEmpty(), () -> "Missing local model textures:\n" + String.join("\n", missingTextures));
	}

	private static List<String> textureReferences(String modelJson) {
		int texturesKey = modelJson.indexOf("\"textures\"");
		if (texturesKey < 0) {
			return List.of();
		}

		int objectStart = modelJson.indexOf('{', texturesKey);
		if (objectStart < 0) {
			return List.of();
		}

		int depth = 0;
		for (int index = objectStart; index < modelJson.length(); index++) {
			char character = modelJson.charAt(index);
			if (character == '{') {
				depth++;
			} else if (character == '}' && --depth == 0) {
				Matcher matcher = TEXTURE_ENTRY.matcher(modelJson.substring(objectStart + 1, index));
				List<String> references = new ArrayList<>();
				while (matcher.find()) {
					references.add(matcher.group(1));
				}
				return references;
			}
		}

		return List.of();
	}

	private static boolean isLocalTexture(String texture) {
		return !texture.contains(":") || texture.startsWith("farmersdelight:");
	}

	private static String texturePath(String texture) {
		return texture.startsWith("farmersdelight:") ? texture.substring("farmersdelight:".length()) : texture;
	}
}
