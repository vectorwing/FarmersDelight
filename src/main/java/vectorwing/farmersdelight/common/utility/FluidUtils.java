package vectorwing.farmersdelight.common.utility;

public class FluidUtils
{
	public static int getBucketAmount(int millibuckets) {
		return millibuckets / 1000;
	}

	public static int getBottleAmount(int millibuckets) {
		return millibuckets / 250;
	}
}
