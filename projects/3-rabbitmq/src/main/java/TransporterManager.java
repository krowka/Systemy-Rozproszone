import java.util.Scanner;

public class TransporterManager {
    private static final int NUMBER_OF_SERVICES = 2;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter transporter name");
        String transporterName = scanner.nextLine();
        System.out.println("Specify " + NUMBER_OF_SERVICES + " type of services, where services are: passengers, cargo, satellite");
        System.out.printf("[%s] >> ", transporterName);

        Service[] services = new Service[NUMBER_OF_SERVICES];
        for (int i = 0; i < services.length; i++) {
            String input = scanner.next();
            Service service = Service.getService(input);
            while (service == null) {
                System.out.printf("Invalid type of service: %s! \nValid services are: passengers, cargo, satellite\n", input);
                input = scanner.next();
                service = Service.getService(input);
            }
            services[i] = service;
        }
        Transporter transporter = new Transporter(transporterName, services);
    }

}
