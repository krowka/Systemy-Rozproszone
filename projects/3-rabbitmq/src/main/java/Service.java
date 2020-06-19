public enum Service {
    PASSENGERS, CARGO, SATELLITE;

    public static Service getService(String serviceName) {
        serviceName = serviceName.toLowerCase();
        return switch (serviceName) {
            case "p", "passengers" -> PASSENGERS;
            case "c", "cargo" -> CARGO;
            case "s", "satellite" -> SATELLITE;
            default -> null;
        };
    }
}
