import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AgencyManager {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter agency name: ");
        String agencyName = br.readLine();
        Agency agency = new Agency(agencyName);
        System.out.println("Enter type of services, where services are: passengers, cargo, satellite");
        System.out.printf("[%s] >> ", agencyName);
        String input;
        while (true) {
            input = br.readLine();
            if (input.equals("exit"))
                break;
            Service service = Service.getService(input);
            if (service == null) {
                System.out.printf("Invalid type of service: %s! \nValid services are: passengers, cargo, satellite\n", input);
                System.out.printf("[%s] >> ", agencyName);
                continue;
            }
            agency.makeRequest(service);
        }
    }
}
